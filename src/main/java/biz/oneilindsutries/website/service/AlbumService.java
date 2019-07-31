package biz.oneilindsutries.website.service;

import biz.oneilindsutries.website.dao.AlbumDAO;
import biz.oneilindsutries.website.entity.Album;
import biz.oneilindsutries.website.validation.GalleryUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AlbumService {

    private final AlbumDAO albumDAO;

    @Autowired
    public AlbumService(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    @Transactional
    public List<Album> getAlbums() {
        return this.albumDAO.getAlbums();
    }

    @Transactional
    public List<Album> getAlbumsByCreator(String name) {
        return this.albumDAO.getAlbumsByCreator(name);
    }

    @Transactional
    public Album getAlbum(int id) {
        return this.albumDAO.getAlbum(id);
    }

    @Transactional
    public Album getAlbumByName(String albumName) {
        return this.albumDAO.getAlbumByName(albumName);
    }

    @Transactional
    public void saveAlbum(Album album) {
        this.albumDAO.saveAlbum(album);
    }

    @Transactional
    public void deleteAlbum(int id) {
        this.albumDAO.deleteAlbum(id);
    }

    @Transactional
    public Album registerAlbum(GalleryUpload galleryUpload, String user) {

        Album album = getAlbumByName(galleryUpload.getAlbumName());

        if (album == null) {
            album = new Album(galleryUpload.getNewalbumName(), user, galleryUpload.getShowUnlistedImages());
            saveAlbum(album);
        }
        return album;
    }
}
