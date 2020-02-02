package biz.oneilindustries.website.service;

import static biz.oneilindustries.website.filecreater.FileHandler.getExtensionType;

import biz.oneilindustries.RandomIDGen;
import biz.oneilindustries.website.dao.MediaDAO;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.entity.PublicMediaApproval;
import biz.oneilindustries.website.exception.MediaApprovalException;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.validation.GalleryUpload;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.apache.tika.Tika;
import org.hibernate.Hibernate;
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
    public List<Media> getAlbumMedias(String id) {
        return this.dao.getAlbumMedias(id);
    }

    @Transactional
    public Media getMedia(int id) {
        return dao.getMedia(id);
    }

    @Transactional
    public Media getMediaWithAlbum(int id) {
        Media media = dao.getMedia(id);
        Hibernate.initialize(media.getAlbum());

        return media;
    }

    @Transactional
    public void saveMedia(Media media) {
        dao.saveMedia(media);
    }

    @Transactional
    public void saveMediaApproval(PublicMediaApproval media) {
        dao.saveMediaApproval(media);
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
    public Media registerMedia(String mediaName, String privacy, File file, String user, Album album) throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        Media media = new Media(mediaName, file.getName() , privacy,user, localDate.format(dtf));

        Optional.ofNullable(album).ifPresent(media::setAlbum);

        Tika tika = new Tika();
        if (FileHandler.isImageFile(file.getName())) {
            media.setMediaType("image");
        } else if (FileHandler.isVideoFile(tika.detect(file))) {
            media.setMediaType("video");
        }
        saveMedia(media);

        return media;
    }

    @Transactional
    public long getTotalMediaCountByUser(String name) {
        return dao.getMediaCountByUser(name);
    }

    @Transactional
    public void resetMediaAlbumIDs(Album album) {
        for (Media media : album.getMedias()) {
            media.setAlbum(null);
            saveMedia(media);
        }
    }

    @Transactional
    public void updateMedia(GalleryUpload galleryUpload, Album album, int mediaID) {
        Media media = getMedia(mediaID);

        media.setName(galleryUpload.getName());
        media.setLinkStatus(galleryUpload.getPrivacy());

        if (album != null) {
            media.setAlbum(album);
        }else {
            media.setAlbum(null);
        }
        saveMedia(media);
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

    @Transactional
    public String generateUniqueMediaName(String originalFileName) {
        String extension = getExtensionType(originalFileName); //
        String fileName = RandomIDGen.getBase62(16) + "." + extension;

        while(this.getMediaFileName(fileName) != null) {
            fileName = RandomIDGen.getBase62(16) + "." + extension;
        }
        return fileName;
    }

    @Transactional
    public List<PublicMediaApproval> getMediaApprovals() {
        return dao.getMediaApprovals();
    }

    @Transactional
    public PublicMediaApproval getMediaApprovalByMediaID(int mediaID) {
        return dao.getMediaApprovalByMediaID(mediaID);
    }

    @Transactional
    public List<PublicMediaApproval> getMediaApprovalsByStatus(String status) {
        return dao.getMediaApprovalsByStatus(status);
    }

    @Transactional
    public void requestPublicApproval(int mediaID, String mediaName, Album album) {
        PublicMediaApproval publicMediaStatus = this.getMediaApprovalByMediaID(mediaID);

        if (publicMediaStatus == null) {
            publicMediaStatus = new PublicMediaApproval(new Media(mediaID), album, mediaName, "pending");
            this.saveMediaApproval(publicMediaStatus);
        } else {
            throw new MediaApprovalException("This media has previously requested public access. Approval status: " + publicMediaStatus
                .getStatus());
        }
    }

    @Transactional
    public void setMediaApprovalStatus(int mediaApprovalID, String newStatus) {
        PublicMediaApproval publicMedia = getMediaApprovalByMediaID(mediaApprovalID);

        if (publicMedia == null) {
            throw new MediaException("Media approval item not found");
        }
        publicMedia.setStatus(newStatus);
        saveMediaApproval(publicMedia);
    }

    @Transactional
    public void deleteMediaApproval(int id) {
        this.dao.deleteMediaApproval(id);
    }

    @Transactional
    public void approvePublicMedia(int mediaID) {
        PublicMediaApproval approval = getMediaApprovalByMediaID(mediaID);

        if (approval == null) {
            throw new MediaApprovalException("No media approval found for media id " + mediaID);
        }
        //Updates media with the approved public details
        Media media = approval.getMedia();
        media.setLinkStatus("public");
        media.setName(approval.getPublicName());
        saveMedia(media);

        this.deleteMediaApproval(approval.getId());
    }
}
