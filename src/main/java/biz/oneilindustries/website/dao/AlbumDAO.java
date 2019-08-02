package biz.oneilindustries.website.dao;

import biz.oneilindustries.website.entity.Album;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlbumDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public AlbumDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Album> getAlbums() {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Album> query = currentSession.createQuery("from Album  order by id desc", Album.class);

        return query.getResultList();
    }

    public List<Album> getAlbumsByCreator(String creator) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Album> query = currentSession.createQuery("from Album where creator=:creator", Album.class);
        query.setParameter("creator",creator);

        return query.getResultList();
    }

    public Album getAlbum(int id) {
        Session currentSession = sessionFactory.getCurrentSession();

        Album album = currentSession.get(Album.class,id);

        return album;
    }

    public Album getAlbumByName(String albumName) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("FROM Album where name=:albumName");
        query.setParameter("albumName",albumName);

        Album album = (Album) query.uniqueResult();

        return album;
    }

    public void saveAlbum(Album album) {
        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(album);
    }

    public void deleteAlbum(int id) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("delete from Album where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public long getAlbumCountByUser(String user) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("select count(creator) from Album where creator=: username");
        query.setParameter("username",user);

        return (long) query.uniqueResult();
    }
}
