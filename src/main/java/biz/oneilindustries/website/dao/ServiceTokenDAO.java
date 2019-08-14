package biz.oneilindustries.website.dao;

import biz.oneilindustries.website.entity.ServiceToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceTokenDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public ServiceTokenDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ServiceToken getToken(String tokenUUID) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from ServiceToken where tokenUUID=:uuid", ServiceToken.class);
        query.setParameter("uuid", tokenUUID);

        return (ServiceToken) query.uniqueResult();
    }

    public void savetoken(ServiceToken token) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(token);
    }

    public void deleteToken(ServiceToken token) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(token);
    }
}
