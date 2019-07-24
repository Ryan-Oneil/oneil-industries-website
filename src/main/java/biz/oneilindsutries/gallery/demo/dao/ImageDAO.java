package biz.oneilindsutries.gallery.demo.dao;

import biz.oneilindsutries.gallery.demo.entity.Image;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Image> getImages() {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<Image> query = currentSession.createQuery("from Image order by id desc", Image.class);

        return query.getResultList();
    }

    public List<Image> getImagesByUser(String username) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<Image> query = currentSession.createQuery("from Image where uploader=:username", Image.class);
        query.setParameter("username",username);

        return query.getResultList();
    }

    public List<Image> getPublicImages() {

        Session currentSession = sessionFactory.getCurrentSession();

        Query<Image> query = currentSession.createQuery("from Image where linkStatus=:linkStatus order by id desc", Image.class);
        query.setParameter("linkStatus","public");

        return query.getResultList();
    }

    public Image getImage(int id) {

        Session currentSession = sessionFactory.getCurrentSession();

        Image image = currentSession.get(Image.class, id);

        return image;
    }

    public Image getImageFileName(String fileName) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query query= currentSession.
                createQuery("from Image where fileName=:fileName");
        query.setParameter("fileName", fileName);
        Image image = (Image) query.uniqueResult();

        return image;
    }

    public void saveImage(Image image) {

        Session currentSession = sessionFactory.getCurrentSession();

        currentSession.saveOrUpdate(image);
    }

    public void deleteImage(int id) {

        Session currentSession = sessionFactory.getCurrentSession();

        Query query = currentSession.createQuery("delete from Image where id=:ImageId");
        query.setParameter("ImageId",id);
        query.executeUpdate();
    }
}
