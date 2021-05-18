package biz.oneilenterprise.website.service;

import static biz.oneilenterprise.website.security.SecurityConstants.SECRET;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import biz.oneilenterprise.website.dto.QuotaDTO;
import biz.oneilenterprise.website.dto.RegisterUserDTO;
import biz.oneilenterprise.website.dto.ShareXConfigDTO;
import biz.oneilenterprise.website.dto.UserDTO;
import biz.oneilenterprise.website.entity.ApiToken;
import biz.oneilenterprise.website.entity.PasswordResetToken;
import biz.oneilenterprise.website.entity.Quota;
import biz.oneilenterprise.website.entity.Role;
import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.entity.VerificationToken;
import biz.oneilenterprise.website.exception.TokenException;
import biz.oneilenterprise.website.exception.UserException;
import biz.oneilenterprise.website.repository.QuotaRepository;
import biz.oneilenterprise.website.repository.ResetPasswordTokenRepository;
import biz.oneilenterprise.website.repository.RoleRepository;
import biz.oneilenterprise.website.repository.UserRepository;
import biz.oneilenterprise.website.repository.VerificationTokenRepository;
import com.auth0.jwt.JWT;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String REGISTRATION_EMAIL_MESSAGE = "You have successfully registered your account. Please confirm your email with this link to complete registration";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ResetPasswordTokenRepository passwordTokenRepository;
    private final QuotaRepository quotaRepository;
    private final RoleRepository roleRepository;
    private final EmailSender emailSender;

    @Value("${service.backendUrl}")
    private String backendUrl;

    @Value("${service.frontendUrl}")
    private String frontendUrl;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
        VerificationTokenRepository verificationTokenRepository,
        ResetPasswordTokenRepository passwordTokenRepository, QuotaRepository quotaRepository, RoleRepository roleRepository,
        EmailSender emailSender) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.quotaRepository = quotaRepository;
        this.roleRepository = roleRepository;
        this.emailSender = emailSender;
    }

    public List<UserDTO> getUsers() {
        return usersToDTOs(userRepository.findAllByOrderByIdAsc());
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

    public void registerUser(RegisterUserDTO registerUserDTO) {
        String username = registerUserDTO.getUsername();
        String email = registerUserDTO.getEmail().toLowerCase();

        validateUsername(username);
        validateEmail(email);

        String encryptedPassword = passwordEncoder.encode(registerUserDTO.getPassword());

        User user = new User(username.toLowerCase(), encryptedPassword,false, email, "ROLE_UNREGISTERED");
        Quota quota = new Quota(user, 0, 25, false);
        user.setQuota(quota);
        userRepository.save(user);

        sendUserVerificationEmail(user);
    }

    public void sendUserVerificationEmail(User user) {
        VerificationToken token = createVerificationToken(user);
        emailSender.sendSimpleEmail(user.getEmail(), "Registration Confirmation", String.format("%s %s/confirmEmail/%s",
            REGISTRATION_EMAIL_MESSAGE, frontendUrl, token.getToken()),"noreply@oneilenterprise.com", null);
    }

    public void validateUsername(String username) {
        if (userRepository.isUsernameTaken(username.toLowerCase())) {
            throw new UserException("Username is taken");
        }
    }

    public void validateEmail(String email) {
        if (userRepository.isEmailTaken(email.toLowerCase())) {
            throw new UserException("Email is taken");
        }
    }

    public void updateUser(UserDTO userDTO, String name) {
        User user = getUser(name);

        Optional<String> email = Optional.ofNullable(userDTO.getEmail());
        Optional<String> password = Optional.ofNullable(userDTO.getPassword());
        Optional<String> role = Optional.ofNullable(userDTO.getRole());

        email.ifPresent(newEmail -> {
            if (!newEmail.equals(user.getEmail())) {
                validateEmail(newEmail);
            }
            user.setEmail(newEmail);
        });
        password.ifPresent(newPassword -> user.setPassword(passwordEncoder.encode(newPassword)));
        role.ifPresent(user::setRole);

        userRepository.save(user);
    }

    public VerificationToken createVerificationToken(User user) {
        String tokenUUID = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken(tokenUUID, user);

        verificationTokenRepository.save(token);

        return token;
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token).orElseThrow(() -> new TokenException("Invalid Verification Token Link"));
    }

    public void deleteVerificationToken(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    public PasswordResetToken generateResetToken(String userEmail) {
        User user = getUserByEmail(userEmail);
        Optional<PasswordResetToken> passwordResetToken = passwordTokenRepository.getByUser(user);
        passwordResetToken.ifPresent(passwordTokenRepository::delete);

        String tokenUUID = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken(tokenUUID,user);
        passwordTokenRepository.save(token);

        emailSender.sendSimpleEmail(user.getEmail(),"Password Reset","Reset Password Link " + frontendUrl + "/changePassword/" + token,"noreply@oneilenterprise.com",null);

        return token;
    }

    public PasswordResetToken getResetToken(String token) {
        return passwordTokenRepository.getByToken(token).orElseThrow(() -> new TokenException("Invalid Reset token"));
    }

    public void resetUserPassword(String tokenUUID, String newPassword) {
        PasswordResetToken token = getResetToken(tokenUUID);

        if (isTokenExpired(token.getExpiryDate())) {
            throw new TokenException("Token has expired");
        }
        changeUserPassword(token.getUser(), newPassword);

        passwordTokenRepository.delete(token);
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    public void updateUserQuota(QuotaDTO quotaDTO, String username) {
        User user = getUser(username);
        Quota quota = user.getQuota();

        quota.setIgnoreQuota(quotaDTO.isIgnoreQuota());
        quota.setMax(quotaDTO.getMax());

        userRepository.save(user);
    }

    public void changeQuotaUsedAmount(String username, long amount) {
        User user = getUser(username);
        Quota quota = user.getQuota();

        quota.setUsed(Math.max(0, quota.getUsed() + amount));
        userRepository.save(user);
    }

    public long getRemainingQuota(String username) {
        AtomicReference<Long> remaining = new AtomicReference<>(0L);
        Quota userQuota = getUser(username).getQuota();

        if (userQuota.isIgnoreQuota()) {
            remaining.set(-1L);
        }else {
            long remainingAmount = (userQuota.getMax() * FileUtils.ONE_GB) - userQuota.getUsed();
            remaining.set(Math.max(remainingAmount, 0));
        }
        return remaining.get();
    }

    @Cacheable(value = "apiToken")
    public ApiToken getApiTokenByUser(User user) {
        User userWithToken = userRepository.findById(user.getId()).orElseThrow(() -> new UsernameNotFoundException(user.getId() + " doesn't exist"));

        return userWithToken.getApiToken();
    }

    @CachePut(value = "apiToken", key = "#result.user")
    public ApiToken generateApiToken(User user) {
        User userWithToken = getUser(user.getUsername());
        // Creates a non expiring user jwt with limited access. Currently can only upload media
        String uuid = UUID.randomUUID().toString();

        String token = JWT.create()
            .withSubject("apiToken")
            .withClaim("userID", user.getId())
            .withClaim("user", user.getUsername())
            .withClaim("role", "ROLE_LIMITED_API")
            .withClaim("enabled", true)
            .withClaim("uuid", uuid)
            .sign(HMAC512(SECRET.getBytes()));

        ApiToken apiToken = new ApiToken(userWithToken, token, uuid);
        userWithToken.setApiToken(apiToken);
        userRepository.save(userWithToken);

        return apiToken;
    }

    public ShareXConfigDTO generateShareXAPIFile(User user) {
        ApiToken apiToken = getApiTokenByUser(user);

        if (apiToken == null) {
            return null;
        }
        //Returns a shareX custom uploader config template
        return new ShareXConfigDTO(apiToken.getToken(), backendUrl);
    }

    private boolean isTokenExpired(Date date) {
        Calendar cal = Calendar.getInstance();
        return (date.getTime() - cal.getTime().getTime()) <= 0;
    }

    public String confirmUserRegistration(String token) {
        VerificationToken verificationToken = getVerificationToken(token);
        User user = verificationToken.getUser();

        if (isTokenExpired(verificationToken.getExpiryDate())) {
            deleteVerificationToken(verificationToken);
            sendUserVerificationEmail(user);

            return "This link has expired, a new one has been emailed";
        }
        deleteVerificationToken(verificationToken);

        user.setEnabled(true);
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "";
    }

    public UserDTO getUserStats(String username) {
        User user = getUser(username);
        Quota quota = user.getQuota();

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

    public void changeUserAccountStatus(boolean status, String username) {
        User user = getUser(username);
        user.setEnabled(status);

        userRepository.save(user);
    }

    // DTOs

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
