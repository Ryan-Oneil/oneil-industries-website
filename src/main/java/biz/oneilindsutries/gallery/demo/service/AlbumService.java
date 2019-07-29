package biz.oneilindsutries.gallery.demo.service;

import biz.oneilindsutries.gallery.demo.dao.AlbumDAO;
import biz.oneilindsutries.gallery.demo.entity.Album;
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

}
