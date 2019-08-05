package biz.oneilindustries.website.dao;

import biz.oneilindustries.website.entity.PasswordResetToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResetPasswordTokenDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public ResetPasswordTokenDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public PasswordResetToken getTokenByUser(String name) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from password_reset_token where username.username=:user");
        query.setParameter("user",name);

        return (PasswordResetToken) query.uniqueResult();
    }

    public PasswordResetToken getToken(String token) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from password_reset_token where token=:token");
        query.setParameter("token",token);

        return (PasswordResetToken) query.uniqueResult();
    }

    public void saveToken(PasswordResetToken token) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(token);
    }

    public void deleteToken(PasswordResetToken token) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(token);
    }
}
