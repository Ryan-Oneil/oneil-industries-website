package biz.oneilindsutries.gallery.demo.service;

import biz.oneilindsutries.gallery.demo.dao.UserDAO;
import biz.oneilindsutries.gallery.demo.entity.Authority;
import biz.oneilindsutries.gallery.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void saveUser(User user) {
        dao.saveUser(user);
    }

    @Transactional
    public void deleteUser(String name) {
        dao.deleteUser(name);
    }

    @Transactional
    public void registerUser(String username, String password, String email) {
        String encryptedPassword = passwordEncoder.encode(password);

        Authority authority = new Authority("ROLE_UNREGISTERED");

        User user = new User(username,encryptedPassword,1,email);

        user.addAuthority(authority);

        saveUser(user);
    }
}
