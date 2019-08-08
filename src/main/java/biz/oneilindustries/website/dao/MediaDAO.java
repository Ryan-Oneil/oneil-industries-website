package biz.oneilindustries.website.dao;

import biz.oneilindustries.website.entity.Media;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MediaDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public MediaDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Media> getImages() {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Media> query = currentSession.createQuery("from Media order by id desc", Media.class);

        return query.getResultList();
    }

    public List<Media> getMediasByUser(String username) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Media> query = currentSession.createQuery("from Media where uploader=:username order by id desc ", Media.class);
        query.setParameter("username",username);

        return query.getResultList();
    }

    public List<Media> getMediasByLinkStatus(String status) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Media> query = currentSession.createQuery("from Media where linkStatus=:status order by id desc", Media.class);
        query.setParameter("status",status);

        return query.getResultList();
    }

    public List<Media> getAlbumMedias(int id) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Media> query = currentSession.createQuery("from Media where albumID =:id");
        query.setParameter("id",id);

        return query.getResultList();
    }

    public Media getMedia(int id) {
        Session currentSession = sessionFactory.getCurrentSession();

        return currentSession.get(Media.class, id);
    }

    public Media getMediaFileName(String fileName) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query= currentSession.
                createQuery("from Media where fileName=:fileName");
        query.setParameter("fileName", fileName);

        return (Media) query.uniqueResult();
    }

    public void saveMedia(Media media) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(media);
    }

    public void deleteMedia(int id) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("delete from Media where id=:ImageId");
        query.setParameter("ImageId",id);
        query.executeUpdate();
    }

    public long getMediaCountByUser(String user) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("select count(uploader) from Media where uploader=: username");
        query.setParameter("username",user);

        return (long) query.uniqueResult();
    }
}
