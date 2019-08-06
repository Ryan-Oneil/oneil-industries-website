package biz.oneilindustries.website.dao;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TokenDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public VerificationToken findByToken(String token) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from VerificationToken where token=:token");
        query.setParameter("token",token);

        return (VerificationToken) query.uniqueResult();
    }

    public VerificationToken findByUser(User user) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from VerificationToken where user=: user");
        query.setParameter("user",user);

        return (VerificationToken) query.uniqueResult();
    }

    public void saveToken(VerificationToken token) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(token);
    }

    public void deleteToken(VerificationToken token) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(token);
    }
}
