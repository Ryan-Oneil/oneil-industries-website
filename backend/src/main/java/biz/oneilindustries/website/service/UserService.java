package biz.oneilindustries.website.service;

import static biz.oneilindustries.AppConfig.BACK_END_URL;
import static biz.oneilindustries.website.security.SecurityConstants.SECRET;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import biz.oneilindustries.website.entity.ApiToken;
import biz.oneilindustries.website.entity.PasswordResetToken;
import biz.oneilindustries.website.entity.Quota;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.exception.UserException;
import biz.oneilindustries.website.repository.APITokenRepository;
import biz.oneilindustries.website.repository.QuotaRepository;
import biz.oneilindustries.website.repository.ResetPasswordTokenRepository;
import biz.oneilindustries.website.repository.UserRepository;
import biz.oneilindustries.website.repository.VerificationTokenRepository;
import biz.oneilindustries.website.validation.LoginForm;
import biz.oneilindustries.website.validation.UpdatedQuota;
import biz.oneilindustries.website.validation.UpdatedUser;
import com.auth0.jwt.JWT;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String USERNAME_REGEX = "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ResetPasswordTokenRepository passwordTokenRepository;
    private final QuotaRepository quotaRepository;
    private final APITokenRepository apiTokenRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
        VerificationTokenRepository verificationTokenRepository,
        ResetPasswordTokenRepository passwordTokenRepository, QuotaRepository quotaRepository,
        APITokenRepository apiTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.quotaRepository = quotaRepository;
        this.apiTokenRepository = apiTokenRepository;
    }

    public List<User> getUsers() {
        return userRepository.getAllUsers();
    }

    public List<User> getRecentUsers() {
        return this.userRepository.getTop5ByOrderByIdDesc();
    }

    public User getUser(String name) {
        return userRepository.getByUsername(name).orElseThrow(() -> new UsernameNotFoundException(name + " doesn't exist"));
    }

    public User getUserByEmail(String email) {
        return userRepository.getUsersByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " doesn't exist"));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User registerUser(LoginForm loginForm) {

        if (!loginForm.getName().matches(USERNAME_REGEX)) {
            throw new UserException("Username may only contain a-Z . _");
        }

        if (userRepository.isUsernameTaken(loginForm.getName())) {
            throw new UserException("An account with this username already exists");
        }

        if (userRepository.isEmailTaken(loginForm.getEmail())) {
            throw new UserException("An account with this email already exists");
        }
        String encryptedPassword = passwordEncoder.encode(loginForm.getPassword());
        String username = loginForm.getName().toLowerCase();

        User user = new User(username, encryptedPassword,false, loginForm.getEmail(), "ROLE_UNREGISTERED");
        Quota quota = new Quota(username, 0, 25, false);

        saveUser(user);
        saveUserQuota(quota);

        return user;
    }

    public void updateUser(UpdatedUser updatedUser, String name) {
        User user = getUser(name);

        if (!updatedUser.getEmail().equals(user.getEmail())) {
            if (userRepository.isEmailTaken(updatedUser.getEmail())) {
                throw new UserException("Email is already registered to another user");
            }
        }
        user.setEmail(updatedUser.getEmail());
        updatedUser.getEnabled().ifPresent(user::setEnabled);
        updatedUser.getPassword().ifPresent(passwordEncoder::encode);
        updatedUser.getRole().ifPresent(user::setRole);

        saveUser(user);
    }

    public void createVerificationToken(User user, String tokenUUID) {
        VerificationToken token = new VerificationToken(tokenUUID, user);

        verificationTokenRepository.save(token);
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token).orElseThrow(() -> new TokenException("Invalid Verification Token Link"));
    }

    public void deleteVerificationToken(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    public String generateResetToken(User user) {
        Optional<PasswordResetToken> passwordResetToken = passwordTokenRepository.getByUsername(user);

        if (passwordResetToken.isPresent()) {
            PasswordResetToken token = passwordResetToken.get();

            if (!isExpired(token.getExpiryDate())) {
                return token.getToken();
            }else {
                //Deletes from database if the existing token is expired
                passwordTokenRepository.delete(token);
            }
        }
        String tokenUUID = UUID.randomUUID().toString();

        PasswordResetToken token = new PasswordResetToken(tokenUUID,user);
        passwordTokenRepository.save(token);

        return tokenUUID;
    }

    public PasswordResetToken getResetToken(String token) {
        return passwordTokenRepository.getByToken(token).orElseThrow(() -> new TokenException("Invalid Reset token"));
    }

    public void resetUserPassword(String tokenUUID, String newPassword) {
        PasswordResetToken token = getResetToken(tokenUUID);

        checkExpired(token.getExpiryDate());
        changeUserPassword(token.getUsername(), newPassword);

        passwordTokenRepository.delete(token);
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));

        saveUser(user);
    }

    public boolean isExpired(Date date) {

        Calendar cal = Calendar.getInstance();
        return (date.getTime() - cal.getTime().getTime()) <= 0;
    }

    public Quota getQuotaByUsername(String username) {
        return quotaRepository.getFirstByUsername(username);
    }

    public void saveUserQuota(Quota quota) {
        quotaRepository.save(quota);
    }

    public void updateUserQuota(UpdatedQuota updatedQuota, String username) {
        Quota quota = getQuotaByUsername(username);

        quota.setIgnoreQuota(updatedQuota.isIgnoreQuota());
        quota.setMax(updatedQuota.getMax());
        saveUserQuota(quota);
    }

    public void increaseUsedAmount(Quota quota, long amount) {
        quota.increaseUsed(amount);

        saveUserQuota(quota);
    }

    public void decreaseUsedAmount(Quota quota, long amount) {
        quota.decreaseUsed(amount);

        saveUserQuota(quota);
    }

    public long getUserRemainingStorage(Quota quota) {
        //Returns -1 if the user is allowed to bypass their limit
        if (quota.isIgnoreQuota()) {
            return -1;
        }
        return Math.max((quota.getMax() * FileUtils.ONE_GB) - quota.getUsed(), 0);
    }

    @Transactional
    @Cacheable(value = "apiToken")
    public ApiToken getApiTokenByUser(String username) {
        return apiTokenRepository.findById(username).orElse(null);
    }

    @Transactional
    @CachePut(value = "apiToken", key = "#result.username")
    public ApiToken generateApiToken(String username) {
        // Creates a non expiring user jwt with limited access. Currently can only upload media
        String uuid = UUID.randomUUID().toString();

        String token = JWT.create()
            .withSubject("apiToken")
            .withClaim("user", username)
            .withClaim("role", "ROLE_LIMITED_API")
            .withClaim("enabled", true)
            .withClaim("uuid", uuid)
            .sign(HMAC512(SECRET.getBytes()));

        ApiToken apiToken = new ApiToken(username, token, uuid);
        apiTokenRepository.save(apiToken);

        return apiToken;
    }

    @Transactional
    @CacheEvict(value = "apiToken", key = "#apiToken.username")
    public void deleteApiToken(ApiToken apiToken) {
        apiTokenRepository.delete(apiToken);
    }

    @Transactional
    public String generateShareXAPIFile(String username) {
        ApiToken apiToken = getApiTokenByUser(username);

        if (apiToken == null) {
            throw new TokenException("Generate a api token first");
        }
        //Returns a shareX custom uploader config template
        return "{\n"
            + "  \"Name\": \"Oneil Industries\",\n"
            + "  \"DestinationType\": \"ImageUploader, TextUploader, FileUploader\",\n"
            + "  \"RequestMethod\": \"POST\",\n"
            + "  \"RequestURL\": \"" + BACK_END_URL + "/gallery/upload\",\n"
            + "  \"Parameters\": {\n"
            + "    \"name\": \"%h.%mi.%s-%d.%mo.%yy\",\n"
            + "    \"privacy\": \"unlisted\",\n"
            + "    \"albumName\": \"none\"\n"
            + "  },\n"
            + "  \"Headers\": {\n"
            + "\"Authorization\": \"Bearer " + apiToken.getToken()
            + "  \"\n},\n"
            + "  \"Body\": \"MultipartFormData\",\n"
            + "  \"FileFormName\": \"file\"\n"
            + "}";
    }

    private void checkExpired(Date date) {

        Calendar cal = Calendar.getInstance();
        if ((date.getTime() - cal.getTime().getTime()) <= 0) {
            throw new TokenException("Token has expired");
        }
    }

    public void confirmUserRegistration(String token) {
        VerificationToken verificationToken = getVerificationToken(token);
        checkExpired(verificationToken.getExpiryDate());

        User user = verificationToken.getUsername();

        deleteVerificationToken(verificationToken);

        user.setEnabled(true);
        saveUser(user);
    }
}
