package biz.oneilindustries.website.dao;

import static biz.oneilindustries.website.security.SecurityConstants.SECRET;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import biz.oneilindustries.website.entity.ApiToken;
import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.Quota;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.entity.User;
import com.auth0.jwt.JWT;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

        Query query = currentSession.createQuery("from users where username =:username",User.class);
        query.setParameter("username", username);

        return (User) query.uniqueResult();
    }

    public User getUserByEmail(String email) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from users where email =:email", User.class);
        query.setParameter("email", email);

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

    public Quota getQuotaByUsername(String username) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from Quota where username=:username", Quota.class);
        query.setParameter("username", username);

        return (Quota) query.uniqueResult();
    }

    public void saveQuota(Quota quota) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(quota);
    }

    public List<String> getApiTokensUUIDByUser(String user) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("select uuid from ApiToken where username=:username", String.class);
        query.setParameter("username", user);

        return query.getResultList();
    }

    public ApiToken getApiTokensByUsername(String user) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("from ApiToken where username=:username", ApiToken.class);
        query.setParameter("username", user);

        return (ApiToken) query.uniqueResult();
    }

    public void saveApiToken(ApiToken apiToken) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(apiToken);
    }

    public void deleteApiToken(ApiToken apiToken) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.delete(apiToken);
    }
}
