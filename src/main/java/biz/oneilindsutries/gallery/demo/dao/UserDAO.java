package biz.oneilindsutries.gallery.demo.dao;

import biz.oneilindsutries.gallery.demo.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<User> getUsers() {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<User> theQuery = currentSession.createQuery("from User", User.class);

        return theQuery.getResultList();
    }

    public User getUser(String username) {

        Session currentSession = sessionFactory.getCurrentSession();

        User user = currentSession.get(User.class, username);

        return user;
    }

    public void saveUser(User user) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(user);
    }

    public void deleteUser(String username) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("delete from User where username=:username");
        query.setParameter("username",username);
        query.executeUpdate();
    }
}
