package biz.oneilindustries.website.service;

import static biz.oneilindustries.AppConfig.BACK_END_URL;
import static biz.oneilindustries.AppConfig.FRONT_END_URL;
import static biz.oneilindustries.AppConfig.GALLERY_IMAGES_DIRECTORY;
import static biz.oneilindustries.website.filecreater.FileHandler.getExtensionType;
import static biz.oneilindustries.website.filecreater.FileHandler.writeImageThumbnail;
import static biz.oneilindustries.website.security.SecurityConstants.TRUSTED_ROLES;

import biz.oneilindustries.RandomIDGen;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.entity.PublicMediaApproval;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.exception.MediaApprovalException;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.repository.AlbumRepository;
import biz.oneilindustries.website.repository.MediaApprovalRepository;
import biz.oneilindustries.website.repository.MediaRepository;
import biz.oneilindustries.website.validation.GalleryUpload;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class MediaService {

    private static final Logger logger = LogManager.getLogger(biz.oneilindustries.website.service.MediaService.class);

    private static final String FILE_NOT_EXISTS_ERROR_MESSAGE = "Media does not exist on this server";
    private static final String PUBLIC = "public";
    private static final String UNLISTED = "unlisted";

    private final MediaRepository mediaRepository;
    private final MediaApprovalRepository approvalRepository;
    private final AlbumRepository albumRepository;

    public MediaService(MediaRepository mediaRepository, MediaApprovalRepository approvalRepository,
        AlbumRepository albumRepository) {
        this.mediaRepository = mediaRepository;
        this.approvalRepository = approvalRepository;
        this.albumRepository = albumRepository;
    }

    public String registerMedias(List<File> mediaFiles, GalleryUpload galleryUpload, User user) throws IOException {
        Album album = null;
        List<Media> mediaList = new ArrayList<>();

        if (galleryUpload.getAlbum() != null || mediaFiles.size() > 1) {
            album = getOrRegisterAlbum(user.getUsername(), galleryUpload.getAlbum());
        }
        for (File mediaFile : mediaFiles) {
            Media media = registerMedia(mediaFile.getName(), galleryUpload.getPrivacy(), mediaFile, user.getUsername(), album);
            checkMediaPrivacy(media, user);

            mediaList.add(media);
        }
        mediaRepository.saveAll(mediaList);
        writeMediaThumbnails(mediaFiles, user.getUsername());

        if (mediaFiles.size() > 1) {
            return  FRONT_END_URL + "/gallery/album/" + album.getId();
        }
        Media media = mediaList.get(0);

        return BACK_END_URL + "/gallery/" + media.getMediaType() + "/" + media.getFileName();
    }

    private void writeMediaThumbnails(List<File> medias, String user) {
        medias.forEach(media -> {
            if (FileHandler.isImageFile(media.getName())) {
                try {
                    writeImageThumbnail(media, String.format("%s/thumbnail/%s/",GALLERY_IMAGES_DIRECTORY, user));
                } catch (IOException e) {
                    logger.error("Error writing thumbnails for image {}", e.getMessage());
                }
            }
        });
    }

    public void checkMediaPrivacy(Media media, User user) {
        if (media.getLinkStatus().equalsIgnoreCase(PUBLIC) && !CollectionUtils.containsAny(user.getAuthorities(), TRUSTED_ROLES)) {
            requestPublicApproval(media.getId(), media.getName(), media.getAlbum());
        }
    }

    public Album getOrRegisterAlbum(String user, String albumName) {
        return albumRepository.getFirstByName(albumName).orElseGet(() -> registerNewAlbum(albumName, user, true));
    }

    public String generateAlbumUUID() {
        String id = RandomIDGen.getBase62(16);
        while(albumRepository.existsById(id)) {
            id = RandomIDGen.getBase62(16);
        }
        return id;
    }

    public Album registerNewAlbum(String albumName, String username, boolean showUnlistedImages) {
        String id = generateAlbumUUID();

        Album album = new Album(id, albumName, username, showUnlistedImages);
        albumRepository.save(album);

        return album;
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

    public HashMap<String, Object> getMediasByUser(String username, Pageable pageable, String mediaType) {
        HashMap<String, Object> medias = new HashMap<>();
        medias.put("medias", mediaRepository.getAllByUploaderAndMediaTypeOrderByIdDesc(username, mediaType, pageable));
        medias.put("total", mediaRepository.getTotalMediasByUserAndType(username, mediaType));

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

    public String getUserMediaDirectory(String username) {
        return String.format("%s%s/",GALLERY_IMAGES_DIRECTORY, username);
    }
}
