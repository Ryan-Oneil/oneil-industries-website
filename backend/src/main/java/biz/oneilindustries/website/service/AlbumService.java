package biz.oneilindustries.website.service;

import biz.oneilindustries.website.dao.AlbumDAO;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.validation.GalleryUpload;
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
            album = new Album(galleryUpload.getNewAlbumName(), user, galleryUpload.getShowUnlistedImages());
            saveAlbum(album);
        }
        return album;
    }

    @Transactional
    public long getAlbumCountByUser(String name) {
        return albumDAO.getAlbumCountByUser(name);
    }

    @Transactional
    public Album updateAlbum(GalleryUpload galleryUpload, String user) {
        Album album = getAlbumByName(galleryUpload.getAlbumName());

        if (album == null) {
            if (getAlbumByName(galleryUpload.getNewAlbumName()) != null) {
                throw new MediaException("This album already exists");
            }
            album = new Album(galleryUpload.getNewAlbumName(),user,galleryUpload.getShowUnlistedImages());
            saveAlbum(album);
        }
        return album;
    }
 }
