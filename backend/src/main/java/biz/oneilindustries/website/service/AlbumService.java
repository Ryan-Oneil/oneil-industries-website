package biz.oneilindustries.website.service;

import biz.oneilindustries.RandomIDGen;
import biz.oneilindustries.website.dao.AlbumDAO;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.pojo.AlbumDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Album> getAlbumsWithMediaByCreator(String name) {
        List<Album> albums = this.albumDAO.getAlbumsByCreator(name);

        for (Album album: albums) {
            Hibernate.initialize(album.getMedias());
        }
        return albums;
    }

    @Transactional
    public Album getAlbum(String id) {
        return this.albumDAO.getAlbum(id);
    }

    @Transactional
    public Album getAlbumByName(String albumName) {
        return this.albumDAO.getAlbumByName(albumName);
    }

    @Transactional
    public Album getAlbumWithMediaByID(String id) {
        Album album = this.albumDAO.getAlbum(id);
        Hibernate.initialize(album.getMedias());

        return album;
    }

    @Transactional
    public void deleteAlbumIfEmpty(String id) {
        Album album = this.getAlbumWithMediaByID(id);

        if (album.getMedias().isEmpty()) {
            this.deleteAlbum(album.getId());
        }
    }

    @Transactional
    public void saveAlbum(Album album) {
        this.albumDAO.saveAlbum(album);
    }

    @Transactional
    public void deleteAlbum(String id) {
        this.albumDAO.deleteAlbum(id);
    }

    @Transactional
    public Album registerAlbum(String albumName, boolean showUnlisted, String user) {
        String id = generateUniqueID();

        Album album = new Album(id, albumName, user, showUnlisted);
        saveAlbum(album);
        return album;
    }

    @Transactional
    public Album registerRandomAlbum(String user) {
        String id = generateUniqueID();

        Album album = new Album(id, id, user, true);
        saveAlbum(album);
        return album;
    }

    private String generateUniqueID() {
        String id = RandomIDGen.getBase62(16);
        while(getAlbumByName(id) != null) {
            id = RandomIDGen.getBase62(16);
        }
        return id;
    }

    @Transactional
    public long getAlbumCountByUser(String name) {
        return albumDAO.getAlbumCountByUser(name);
    }

    @Transactional
    public void updateAlbum(String albumID, String name, boolean showUnlistedMedia) {
        Album album = getAlbum(albumID);

        if (album == null) {
            throw new MediaException("This album doesn't exists");
        }
        album.setName(name);
        album.setShowUnlistedImages(showUnlistedMedia);

        saveAlbum(album);
    }

    @Transactional
    public List<AlbumDetails> getAlbumDetailsByCreator(String user) {

        List<Album> albums = albumDAO.getAlbumsByCreator(user);
        List<AlbumDetails> albumDetails = new ArrayList<>();

        for (Album album : albums) {
            albumDetails.add(new AlbumDetails(album.getId(), album.getName(), album.isShowUnlistedImages()));
        }
        return albumDetails;
    }

    @Transactional
    public Album getOrRegisterAlbum(String albumName, boolean showUnlisted, String user) {
        return  Optional.ofNullable(getAlbum(albumName))
            //Album name passed didn't exist, creates a new one with the new album name passed
            .orElseGet(() -> registerAlbum(albumName, showUnlisted, user));
    }
}
