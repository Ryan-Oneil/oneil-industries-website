package biz.oneilindustries.website.dao;

import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<User> getUsers() {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<User> theQuery = currentSession.createQuery("from users", User.class);

        return theQuery.getResultList();
    }

    public User getUser(String username) {

        Session currentSession = sessionFactory.getCurrentSession();

        return currentSession.get(User.class, username);
    }

    public User getUserByEmail(String email) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from users where email =:email",User.class);
        query.setParameter("email",email);

        return (User) query.uniqueResult();
    }

    public void saveUser(User user) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(user);
    }

    public void deleteUser(String username) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("delete from users where username=:username");
        query.setParameter("username",username);
        query.executeUpdate();
    }

    public List<DiscordUser> getUsersDiscordProfile(String username) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<DiscordUser> query = currentSession.createQuery("from DiscordUser where username=:username", DiscordUser.class);
        query.setParameter("username", username);

        return query.getResultList();
    }

    public List<TeamspeakUser> getUserTeamspeakProfile(String username) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<TeamspeakUser> query = currentSession.createQuery("from TeamspeakUser where username=:username", TeamspeakUser.class);
        query.setParameter("username", username);

        return query.getResultList();
    }

    public void saveTeamspeakProfile(TeamspeakUser teamspeakUser) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(teamspeakUser);
    }

    public void saveDiscordProfile(DiscordUser discordUser) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(discordUser);
    }

    public TeamspeakUser getTeamspeakUUID(String uuid) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from TeamspeakUser where uuid=:uuid", TeamspeakUser.class);
        query.setParameter("uuid", uuid);

        return (TeamspeakUser) query.uniqueResult();
    }

    public DiscordUser getDiscordUUID(String uuid) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from DiscordUser where uuid=:uuid", DiscordUser.class);
        query.setParameter("uuid", uuid);

        return (DiscordUser) query.uniqueResult();
    }

    public TeamspeakUser getTeamspeakByID(int id) {

        Session currentSession = sessionFactory.getCurrentSession();

        return currentSession.get(TeamspeakUser.class, id);
    }

    public DiscordUser getDiscordByID(int id) {

        Session currentSession = sessionFactory.getCurrentSession();

        return currentSession.get(DiscordUser.class, id);
    }

    public void deleteDiscordUUID(DiscordUser discordUser) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(discordUser);
    }

    public void deleteTeamspeakUUID(TeamspeakUser teamspeakUser) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(teamspeakUser);
    }

    public List<String> getTeamspeakUUIDs() {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<String> query = currentSession.createQuery("select uuid from TeamspeakUser");

        return query.getResultList();
    }

    public List<String> getDiscordUUIDs() {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<String> query = currentSession.createQuery("select uuid from DiscordUser");

        return query.getResultList();
    }
}
