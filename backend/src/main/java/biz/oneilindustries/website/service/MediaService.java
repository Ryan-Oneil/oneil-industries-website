package biz.oneilindustries.website.service;

import biz.oneilindustries.website.dao.MediaDAO;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.validation.GalleryUpload;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaService {

    private final MediaDAO dao;

    @Autowired
    public MediaService(MediaDAO dao) {
        this.dao = dao;
    }

    @Transactional
    public List<Media> getMedias() {
        return dao.getImages();
    }

    @Transactional
    public List<Media> getMediaByLinkStatus(String status) {
        return dao.getMediasByLinkStatus(status);
    }

    @Transactional
    public List<Media> getMediasByUser(String username) {
        return this.dao.getMediasByUser(username);
    }

    @Transactional
    public List<Media> getAlbumMedias(int id) {
        return this.dao.getAlbumMedias(id);
    }

    @Transactional
    public Media getMedia(int id) {
        return dao.getMedia(id);
    }

    @Transactional
    public void saveMedia(Media media) {
        dao.saveMedia(media);
    }

    @Transactional
    public void deleteMedia(int id) {
        dao.deleteMedia(id);
    }

    @Transactional
    public Media getMediaFileName(String fileName) {
        return dao.getMediaFileName(fileName);
    }

    @Transactional
    public void registerMedia(GalleryUpload galleryUpload, String user, Album album) throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        Media media = new Media(galleryUpload.getName(), galleryUpload.getFile().getName() ,galleryUpload.getPrivacy(),user, localDate.format(dtf));

        if (album != null) {
            media.setAlbumID(album.getId());
        }
        Tika tika = new Tika();
        if (FileHandler.isImageFile(galleryUpload.getFile().getName())) {
            media.setMediaType("image");
        } else if (FileHandler.isVideoFile(tika.detect(galleryUpload.getFile()))) {
            media.setMediaType("video");
        }
        saveMedia(media);
    }

    @Transactional
    public long getTotalMediaCountByUser(String name) {
        return dao.getMediaCountByUser(name);
    }

    @Transactional
    public void resetMediaAlbumIDs(Album album) {
        List<Media> albumMedia = getAlbumMedias(album.getId());

        for (Media media : albumMedia) {
            media.setAlbumID(null);
            saveMedia(media);
        }
    }

    @Transactional
    public Media updateMedia(GalleryUpload galleryUpload, Album album, int mediaID) {
        Media media = getMedia(mediaID);

        media.setName(galleryUpload.getName());
        media.setLinkStatus(galleryUpload.getPrivacy());

        if (album != null) {
            media.setAlbumID(album.getId());
        }else {
            media.setAlbumID(null);
        }
        saveMedia(media);
        return media;
    }

    @Transactional
    public void hideAllMedia(String name) {
        List<Media> userMedias = getMediasByUser(name);

        if (userMedias == null) return;

        for (Media media : userMedias) {
            media.setLinkStatus("private");
            saveMedia(media);
        }
    }
}
