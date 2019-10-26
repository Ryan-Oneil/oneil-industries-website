package biz.oneilindustries.website.service;

import biz.oneilindustries.website.dao.ResetPasswordTokenDAO;
import biz.oneilindustries.website.dao.TokenDAO;
import biz.oneilindustries.website.dao.UserDAO;
import biz.oneilindustries.website.entity.Authority;
import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.PasswordResetToken;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.validation.LoginForm;
import biz.oneilindustries.website.validation.UpdatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

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
        String encryptedPassword = passwordEncoder.encode(loginForm.getPassword());

        Authority authority = new Authority("ROLE_UNREGISTERED");

        User user = new User(loginForm.getName(),encryptedPassword,0,loginForm.getEmail());

        user.addAuthority(authority);

        saveUser(user);

        return user;
    }

    @Transactional
    public void updateUser(UpdatedUser updatedUser, String name) throws UsernameNotFoundException {
        User user = getUser(name);

        if (user == null) {
            throw new UsernameNotFoundException(name + " doesn't exists");
        }

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setEnabled(updatedUser.getEnabled());

        //For my system I want users to only have one role/authority at a time
        user.getCustomAuthorities().get(0).setAuthority(updatedUser.getRole());

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
    public void generateResetToken(User user, String token) {

        PasswordResetToken passwordResetToken = passwordTokenDAO.getTokenByUser(user.getUsername());

        if (passwordResetToken != null && !isExpired(passwordResetToken.getExpiryDate())) {
            throw new TokenException("A reset link has already been emailed to you");
        }

        passwordResetToken = new PasswordResetToken(token,user);

        passwordTokenDAO.saveToken(passwordResetToken);
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
}
