package biz.oneilindsutries.website.service;

import biz.oneilindsutries.website.dao.UserDAO;
import biz.oneilindsutries.website.entity.Authority;
import biz.oneilindsutries.website.entity.User;
import biz.oneilindsutries.website.validation.LoginForm;
import biz.oneilindsutries.website.validation.UpdatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserDAO dao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
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
    public void registerUser(LoginForm loginForm) {
        String encryptedPassword = passwordEncoder.encode(loginForm.getPassword());

        Authority authority = new Authority("ROLE_UNREGISTERED");

        User user = new User(loginForm.getName(),encryptedPassword,1,loginForm.getEmail());

        user.addAuthority(authority);

        saveUser(user);
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
}
