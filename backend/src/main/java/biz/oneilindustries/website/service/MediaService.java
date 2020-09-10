package biz.oneilindustries.website.service;

import static biz.oneilindustries.website.filecreater.FileHandler.getExtensionType;

import biz.oneilindustries.RandomIDGen;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.entity.PublicMediaApproval;
import biz.oneilindustries.website.exception.MediaApprovalException;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.repository.MediaApprovalRepository;
import biz.oneilindustries.website.repository.MediaRepository;
import biz.oneilindustries.website.validation.GalleryUpload;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.apache.tika.Tika;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MediaService {

    private static final String FILE_NOT_EXISTS_ERROR_MESSAGE = "Media does not exist on this server";
    private static final String PUBLIC = "public";

    private final MediaRepository mediaRepository;
    private final MediaApprovalRepository approvalRepository;

    public MediaService(MediaRepository mediaRepository, MediaApprovalRepository approvalRepository) {
        this.mediaRepository = mediaRepository;
        this.approvalRepository = approvalRepository;
    }

    public List<Media> getMedias(Pageable pageable) {
        return mediaRepository.getAllByOrderByDateAddedDesc(pageable);
    }

    public HashMap<String, Object> getPublicMedias(Pageable pageable) {
        HashMap<String, Object> publicMedias = new HashMap<>();
        publicMedias.put("medias", mediaRepository.getAllByLinkStatus(PUBLIC, pageable));
        publicMedias.put("total", mediaRepository.getTotalMediaByStatus(PUBLIC));

        return publicMedias;
    }

    public HashMap<String, Object> getMediasByUser(String username, Pageable pageable) {
        HashMap<String, Object> medias = new HashMap<>();
        medias.put("medias", mediaRepository.getAllByUploader(username, pageable));
        medias.put("total", mediaRepository.getTotalMediasByUser(username));

        return medias;
    }

    public Media getMedia(int id) {
        return mediaRepository.findById(id).orElseThrow(() -> new MediaException(FILE_NOT_EXISTS_ERROR_MESSAGE));
    }

    public void saveMedia(Media media) {
        mediaRepository.save(media);
    }

    public void saveMediaApproval(PublicMediaApproval media) {
        approvalRepository.save(media);
    }

    public void deleteMedia(int id) {
        mediaRepository.deleteById(id);
    }

    public Media getMediaFileName(String fileName) {
        return mediaRepository.getFirstByFileName(fileName).orElseThrow(() -> new MediaException(FILE_NOT_EXISTS_ERROR_MESSAGE));
    }

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

    public void resetMediaAlbumIDs(Album album) {
        album.getMedias().forEach(media -> media.setAlbum(null));
        mediaRepository.saveAll(album.getMedias());
    }

    public void updateMedia(GalleryUpload galleryUpload, Album album, int mediaID) {
        Media media = getMedia(mediaID);

        media.setName(galleryUpload.getName());
        media.setLinkStatus(galleryUpload.getPrivacy());
        media.setAlbum(album);

        saveMedia(media);
    }

    public void hideAllMedia(String name) {
//        List<Media> userMedias = getMediasByUser(name);
//
//        for (Media media : userMedias) {
//            media.setLinkStatus("private");
//        }
//        mediaRepository.saveAll(userMedias);
    }

    public String generateUniqueMediaName(String originalFileName) {
        String extension = getExtensionType(originalFileName);
        String fileName = RandomIDGen.getBase62(16) + "." + extension;

        while(mediaRepository.isFileNameTaken(fileName)) {
            fileName = RandomIDGen.getBase62(16) + "." + extension;
        }
        return fileName;
    }

    public PublicMediaApproval getMediaApprovalByMediaID(int mediaID) {
        return approvalRepository.getFirstByMedia_Id(mediaID).orElse(null);
    }

    public List<PublicMediaApproval> getMediaApprovalsByStatus(String status) {
        return approvalRepository.findAllByStatus(status);
    }

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

    public void setMediaApprovalStatus(int mediaApprovalID, String newStatus) {
        PublicMediaApproval publicMedia = getMediaApprovalByMediaID(mediaApprovalID);

        if (publicMedia == null) {
            throw new MediaException("Media approval item not found");
        }
        publicMedia.setStatus(newStatus);
        saveMediaApproval(publicMedia);
    }

    public void deleteMediaApproval(int id) {
        this.approvalRepository.deleteById(id);
    }

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

    public long getTotalMedias() {
        return mediaRepository.count();
    }
}
