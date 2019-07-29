package biz.oneilindsutries.gallery.demo.service;

import biz.oneilindsutries.gallery.demo.dao.ImageDAO;
import biz.oneilindsutries.gallery.demo.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ImageService {

    private final ImageDAO dao;

    @Autowired
    public ImageService(ImageDAO dao) {
        this.dao = dao;
    }

    @Transactional
    public List<Image> getImages() {
        return dao.getImages();
    }

    @Transactional
    public List<Image> getImagesByLinkStatus(String status) {
        return dao.getImagesByLinkStatus(status);
    }

    @Transactional
    public List<Image> getImagesByUser(String username) {
        return this.dao.getImagesByUser(username);
    }

    @Transactional
    public List<Image> getAlbumImages(int id) {
        return this.dao.getAlbumImages(id);
    }

    @Transactional
    public Image getImage(int id) {
        return dao.getImage(id);
    }

    @Transactional
    public void saveImage(Image image) {
        dao.saveImage(image);
    }

    @Transactional
    public void deleteImage(int id) {
        dao.deleteImage(id);
    }

    @Transactional
    public Image getImageFileName(String fileName) {
        return dao.getImageFileName(fileName);
    }
}
