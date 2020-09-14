package biz.oneilindustries.website.service;

import static biz.oneilindustries.AppConfig.BACK_END_URL;
import static biz.oneilindustries.website.security.SecurityConstants.SECRET;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import biz.oneilindustries.website.dto.QuotaDTO;
import biz.oneilindustries.website.dto.UserDTO;
import biz.oneilindustries.website.entity.ApiToken;
import biz.oneilindustries.website.entity.PasswordResetToken;
import biz.oneilindustries.website.entity.Quota;
import biz.oneilindustries.website.entity.Role;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.exception.UserException;
import biz.oneilindustries.website.repository.APITokenRepository;
import biz.oneilindustries.website.repository.QuotaRepository;
import biz.oneilindustries.website.repository.ResetPasswordTokenRepository;
import biz.oneilindustries.website.repository.RoleRepository;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
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
    private final RoleRepository roleRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
        VerificationTokenRepository verificationTokenRepository,
        ResetPasswordTokenRepository passwordTokenRepository, QuotaRepository quotaRepository,
        APITokenRepository apiTokenRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.quotaRepository = quotaRepository;
        this.apiTokenRepository = apiTokenRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDTO> getUsers() {
        return usersToDTOs(userRepository.getAllUsers());
    }

    public List<UserDTO> getRecentUsers() {
        return usersToDTOs(this.userRepository.getTop5ByOrderByIdDesc());
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
        String username = loginForm.getName();
        String email = loginForm.getEmail();

        validateUsername(username);
        validateEmail(email);

        String encryptedPassword = passwordEncoder.encode(loginForm.getPassword());

        User user = new User(username.toLowerCase(), encryptedPassword,false, email, "ROLE_UNREGISTERED");
        Quota quota = new Quota(username, 0, 25, false);

        saveUser(user);
        quotaRepository.save(quota);

        return user;
    }

    public void validateUsername(String username) {
        if (!username.matches(USERNAME_REGEX)) {
            throw new UserException("Username may only contain a-Z . _");
        }

        if (userRepository.isUsernameTaken(username.toLowerCase())) {
            throw new UserException("Username is taken");
        }
    }

    public void validateEmail(String email) {
        if (userRepository.isEmailTaken(email.toLowerCase())) {
            throw new UserException("Email is taken");
        }
    }

    public void updateUser(UpdatedUser updatedUser, String name) {
        User user = getUser(name);

        if (!updatedUser.getEmail().equals(user.getEmail())) {
            validateEmail(updatedUser.getEmail());
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

    public void increaseUsedAmount(String username, long amount) {
        Optional<Quota> userQuota = quotaRepository.findById(username);

        userQuota.ifPresent(quota -> {
            quota.increaseUsed(amount);
            quotaRepository.save(quota);
        });
    }

    public void decreaseUsedAmount(Quota quota, long amount) {
        quota.decreaseUsed(amount);

        saveUserQuota(quota);
    }

    public long getRemainingQuota(String username) {
        AtomicReference<Long> remaining = new AtomicReference<>(0L);

        Optional<Quota> userQuota = quotaRepository.findById(username);

        userQuota.ifPresent(quota -> {
            if (quota.isIgnoreQuota()) {
                remaining.set(-1L);
            }else {
                long remainingAmount = (quota.getMax() * FileUtils.ONE_GB) - quota.getUsed();
                remaining.set(Math.max(remainingAmount, 0));
            }
        });
        return remaining.get();
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

    public UserDTO getUserStats(String username) {
        Quota quota = getQuotaByUsername(username);
        User user = getUser(username);

        QuotaDTO quotaDTO = quotaToDTO(quota);
        UserDTO userDTO = userToDTO(user);
        userDTO.setQuota(quotaDTO);

        return userDTO;
    }

    public long getTotalUsedQuota() {
        return quotaRepository.getTotalUsed();
    }

    public List<Role> getRoles() {
        return  roleRepository.getAllRoles();
    }

    public QuotaDTO quotaToDTO(Quota quota) {
        return new QuotaDTO(quota.getUsed(), quota.getMax(), quota.isIgnoreQuota());
    }

    public List<UserDTO> usersToDTOs(List<User> users) {
        return users.stream()
            .map(this::userToDTO)
            .collect(Collectors.toList());
    }

    public UserDTO userToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getEnabled());
    }
}
