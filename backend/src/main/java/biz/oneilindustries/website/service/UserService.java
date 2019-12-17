package biz.oneilindustries.website.service;

import static biz.oneilindustries.website.security.SecurityConstants.SECRET;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import biz.oneilindustries.website.dao.ResetPasswordTokenDAO;
import biz.oneilindustries.website.dao.TokenDAO;
import biz.oneilindustries.website.dao.UserDAO;
import biz.oneilindustries.website.entity.ApiToken;
import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.PasswordResetToken;
import biz.oneilindustries.website.entity.Quota;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import biz.oneilindustries.website.exception.UserException;
import biz.oneilindustries.website.validation.LoginForm;
import biz.oneilindustries.website.validation.UpdatedQuota;
import biz.oneilindustries.website.validation.UpdatedUser;
import com.auth0.jwt.JWT;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public static final String USERNAME_REGEX = "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";

    @Autowired
    private UserDAO dao;

    @Autowired
    private TokenDAO tokenDAO;

    @Autowired
    private ResetPasswordTokenDAO passwordTokenDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public List<User> getUsers() {
        return dao.getUsers();
    }

    @Transactional
    public User getUser(String name) {
        return dao.getUser(name);
    }

    @Transactional
    public User getUserByEmail(String email) {
        return dao.getUserByEmail(email);
    }

    @Transactional
    public void saveUser(User user) {
        dao.saveUser(user);
    }

    @Transactional
    public void deleteUser(String name) {
        dao.deleteUser(name);
    }

    @Transactional
    public User registerUser(LoginForm loginForm) {

        if (!loginForm.getName().matches(USERNAME_REGEX)) {
            throw new UserException("Username may only contain a-Z . _");
        }
        String encryptedPassword = passwordEncoder.encode(loginForm.getPassword());
        String username = loginForm.getName();

        User user = new User(username, encryptedPassword,false, loginForm.getEmail(), "ROLE_UNREGISTERED");

        Quota quota = new Quota(loginForm.getName(), 0, 25, false);

        saveUser(user);
        saveUserQuota(quota);

        return user;
    }

    @Transactional
    public void updateUser(UpdatedUser updatedUser, String name) throws UsernameNotFoundException {
        User user = getUser(name);

        if (user == null) {
            throw new UsernameNotFoundException(name + " doesn't exists");
        }

        if (updatedUser.getUsername() != null) {
            user.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getEnabled() != null) {
            user.setEnabled(updatedUser.getEnabled());
        }

        if (updatedUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }
        saveUser(user);
    }

    @Transactional
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        saveVerificationToken(myToken);
    }

    @Transactional
    public VerificationToken getToken(String token) {
        return tokenDAO.findByToken(token);
    }

    @Transactional
    public VerificationToken getTokenByUser(User user) {
        return tokenDAO.findByUser(user);
    }

    @Transactional
    public void saveVerificationToken(VerificationToken token) {
        tokenDAO.saveToken(token);
    }

    @Transactional
    public void deleteVerificationToken(VerificationToken token) {
        tokenDAO.deleteToken(token);
    }

    @Transactional
    public String generateResetToken(User user) {

        PasswordResetToken passwordResetToken = passwordTokenDAO.getTokenByUser(user.getUsername());

        if (passwordResetToken != null) {
            if (!isExpired(passwordResetToken.getExpiryDate())) {
                return passwordResetToken.getToken();
            }else {
                //Deletes from database if the existing token is expired
                deletePasswordResetToken(passwordResetToken);
            }
        }
        String token = UUID.randomUUID().toString();

        passwordResetToken = new PasswordResetToken(token,user);
        passwordTokenDAO.saveToken(passwordResetToken);

        return token;
    }

    @Transactional
    public PasswordResetToken getResetToken(String token) {
        return passwordTokenDAO.getToken(token);
    }

    @Transactional
    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));

        saveUser(user);
    }

    @Transactional
    public void deletePasswordResetToken(PasswordResetToken token) {
        passwordTokenDAO.deleteToken(token);
    }

    public boolean isExpired(Date date) {

        Calendar cal = Calendar.getInstance();
        return (date.getTime() - cal.getTime().getTime()) <= 0;
    }

    @Transactional
    public List<DiscordUser> getUserDiscordProfiles(String username) {
        return dao.getUsersDiscordProfile(username);
    }

    @Transactional
    public List<TeamspeakUser> getUserTeamspeakProfile(String username) {
        return dao.getUserTeamspeakProfile(username);
    }

    @Transactional
    public void saveUserTeamspeakProfile(TeamspeakUser teamspeakUser) {
        dao.saveTeamspeakProfile(teamspeakUser);
    }

    @Transactional
    public void saveUserDiscordProfile(DiscordUser discordUser) {
        dao.saveDiscordProfile(discordUser);
    }

    @Transactional
    public TeamspeakUser getTeamspeakUUID(String uuid) {
        return dao.getTeamspeakUUID(uuid);
    }

    @Transactional
    public DiscordUser getDiscordUUID(String uuid) {
        return dao.getDiscordUUID(uuid);
    }

    @Transactional
    public List<String> getTeamspeakUUIDs() {
        return dao.getTeamspeakUUIDs();
    }

    @Transactional
    public List<String> getDiscordUUIDs() {
        return dao.getDiscordUUIDs();
    }

    @Transactional
    public TeamspeakUser getTeamspeakByID(int id) {
        return dao.getTeamspeakByID(id);
    }

    @Transactional
    public DiscordUser getDiscordById(int id) {
        return dao.getDiscordByID(id);
    }

    @Transactional
    public void deleteTeamspeakUUID(TeamspeakUser teamspeakUser) {
        dao.deleteTeamspeakUUID(teamspeakUser);
    }

    @Transactional
    public void deleteDiscordUUID(DiscordUser discordUser) {
        dao.deleteDiscordUUID(discordUser);
    }

    @Transactional
    public Quota getQuotaByUsername(String username) {
        return dao.getQuotaByUsername(username);
    }

    @Transactional
    public void saveUserQuota(Quota quota) {
        dao.saveQuota(quota);
    }

    @Transactional
    public void updateUserQuota(UpdatedQuota updatedQuota, String username) {
        Quota quota = getQuotaByUsername(username);

        if (quota == null) {
            throw new UserException("No user found");
        }
        quota.setIgnoreQuota(updatedQuota.isIgnoreQuota());
        quota.setMax(updatedQuota.getMax());
        saveUserQuota(quota);
    }

    @Transactional
    public void increaseUsedAmount(Quota quota, long amount) {
        quota.increaseUsed(amount);

        dao.saveQuota(quota);
    }

    @Transactional
    public void decreaseUsedAmount(Quota quota, long amount) {
        quota.decreaseUsed(amount);

        dao.saveQuota(quota);
    }

    @Transactional
    @Cacheable("apiToken")
    public List<String> getApiTokensUUIDByUser(String username) {
        return dao.getApiTokensUUIDByUser(username);
    }

    @Transactional
    public ApiToken getApiTokensByUsername(String username) {
        return dao.getApiTokensByUsername(username);
    }

    @Transactional
    @CachePut(value = "apiToken", key = "#result.uuid")
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

        dao.saveApiToken(apiToken);

        return apiToken;
    }

    @Transactional
    public void deleteApiToken(ApiToken apiToken) {
        dao.deleteApiToken(apiToken);
    }

    @Transactional
    public String generateShareXAPIFile(String username) {
        String fileLocation = "E:/media/" + username + "/";

        File file = new File(fileLocation);
        if (!file.exists()) {
            file.mkdir();
        }
        ApiToken apiToken = getApiTokensByUsername(username);

        if (apiToken == null) {
            apiToken = generateApiToken(username);
        }
        //Returns a shareX custom uploader config template
        return "{\n"
                + "  \"Name\": \"Oneil Industries\",\n"
                + "  \"DestinationType\": \"ImageUploader, TextUploader, FileUploader\",\n"
                + "  \"RequestMethod\": \"POST\",\n"
                + "  \"RequestURL\": \"http://localhost:8080/api/gallery/upload\",\n"
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
}
