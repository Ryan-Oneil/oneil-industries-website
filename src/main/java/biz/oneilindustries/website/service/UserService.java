package biz.oneilindustries.website.service;

import biz.oneilindustries.website.dao.ResetPasswordTokenDAO;
import biz.oneilindustries.website.dao.TokenDAO;
import biz.oneilindustries.website.dao.UserDAO;
import biz.oneilindustries.website.entity.Authority;
import biz.oneilindustries.website.entity.PasswordResetToken;
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

    private final UserDAO dao;

    private final TokenDAO tokenDAO;

    private final ResetPasswordTokenDAO passwordTokenDAO;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO dao, TokenDAO tokenDAO, ResetPasswordTokenDAO passwordTokenDAO, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.tokenDAO = tokenDAO;
        this.passwordTokenDAO = passwordTokenDAO;
        this.passwordEncoder = passwordEncoder;
    }

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
        user.getCustomaAthorities().get(0).setAuthority(updatedUser.getRole());

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
}
