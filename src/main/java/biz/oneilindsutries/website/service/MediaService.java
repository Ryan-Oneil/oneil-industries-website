package biz.oneilindsutries.website.service;

import biz.oneilindsutries.website.dao.MediaDAO;
import biz.oneilindsutries.website.entity.Album;
import biz.oneilindsutries.website.entity.Media;
import biz.oneilindsutries.website.filecreater.FileHandler;
import biz.oneilindsutries.website.validation.GalleryUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public void registerMedia(GalleryUpload galleryUpload, String user, Album album) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        Media media = new Media(galleryUpload.getName(), galleryUpload.getFile().getOriginalFilename() ,galleryUpload.getPrivacy(),user, localDate.format(dtf));

        if (album != null) {
            media.setAlbumID(album.getId());
        }

        if (FileHandler.isImageFile(galleryUpload.getFile().getOriginalFilename())) {
            media.setMediaType("image");
        }else if (FileHandler.isVideoFile(galleryUpload.getFile().getContentType())) {
            media.setMediaType("video");
        }

        saveMedia(media);
    }
}
