package biz.oneilindustries.website.service;

import biz.oneilindustries.website.dao.MediaDAO;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.entity.PublicMediaApproval;
import biz.oneilindustries.website.exception.MediaApprovalException;
import biz.oneilindustries.website.exception.MediaException;
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
    public void registerMedia(GalleryUpload galleryUpload, String user, Album album, boolean needsApproval) throws IOException {

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

        if (needsApproval) {
            PublicMediaApproval publicMedia = new PublicMediaApproval(media, album, galleryUpload.getName(), "pending");
            saveMediaApproval(publicMedia);
        }
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
    public void updateMedia(GalleryUpload galleryUpload, Album album, int mediaID) {
        Media media = getMedia(mediaID);

        media.setName(galleryUpload.getName());
        media.setLinkStatus(galleryUpload.getPrivacy());

        if (album != null) {
            media.setAlbumID(album.getId());
        }else {
            media.setAlbumID(null);
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
