package biz.oneilindustries.website.dao;

import biz.oneilindustries.website.entity.FeedBack;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedBackDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<FeedBack> getFeedbacks() {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<FeedBack> query = currentSession.createQuery("from FeedBack ", FeedBack.class);

        return query.getResultList();
    }

    public List<FeedBack> getFeedbacksByIP(String ip) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<FeedBack> query = currentSession.createQuery("from FeedBack where ipAddress=:ip", FeedBack.class);
        query.setParameter("ip",ip);

        return query.getResultList();
    }

    public void saveFeedback(FeedBack feedBack) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(feedBack);
    }

    public FeedBack getFeedbackByID(int id) {

        Session currentSession = sessionFactory.getCurrentSession();

        return currentSession.get(FeedBack.class, id);
    }

    public void deleteFeedback(FeedBack feedBack) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(feedBack);
    }
}
