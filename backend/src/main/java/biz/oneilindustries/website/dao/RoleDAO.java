package biz.oneilindustries.website.dao;

import biz.oneilindustries.website.entity.Role;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Role> getRoles() {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Role> query = currentSession.createQuery("from Role", Role.class);

        return query.getResultList();
    }
}
