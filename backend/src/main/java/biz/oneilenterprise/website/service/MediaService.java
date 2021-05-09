package biz.oneilenterprise.website.service;

import static biz.oneilenterprise.website.filecreater.FileHandler.writeImageThumbnail;
import static biz.oneilenterprise.website.filecreater.FileHandler.writeVideoThumbnail;
import static biz.oneilenterprise.website.security.SecurityConstants.TRUSTED_ROLES;

import biz.oneilenterprise.website.utils.RandomIDGen;
import biz.oneilenterprise.website.dto.AlbumDTO;
import biz.oneilenterprise.website.dto.MediaDTO;
import biz.oneilenterprise.website.entity.Album;
import biz.oneilenterprise.website.entity.Media;
import biz.oneilenterprise.website.entity.PublicMediaApproval;
import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.enums.MediaType;
import biz.oneilenterprise.website.exception.AlbumMissingException;
import biz.oneilenterprise.website.exception.MediaApprovalException;
import biz.oneilenterprise.website.exception.MediaException;
import biz.oneilenterprise.website.filecreater.FileHandler;
import biz.oneilenterprise.website.repository.AlbumRepository;
import biz.oneilenterprise.website.repository.MediaApprovalRepository;
import biz.oneilenterprise.website.repository.MediaRepository;
import biz.oneilenterprise.website.dto.GalleryUploadDTO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class MediaService {

    private static final Logger logger = LogManager.getLogger(biz.oneilenterprise.website.service.MediaService.class);

    private static final String FILE_NOT_EXISTS_ERROR_MESSAGE = "Media does not exist on this server";
    private static final String PUBLIC = "public";
    private static final String UNLISTED = "unlisted";

    private final MediaRepository mediaRepository;
    private final MediaApprovalRepository approvalRepository;
    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;

    @Value("${service.media.location}")
    private String mediaDirectory;

    @Value("${service.backendUrl}")
    private String backendUrl;

    public MediaService(MediaRepository mediaRepository, MediaApprovalRepository approvalRepository,
        AlbumRepository albumRepository, ModelMapper modelMapper) {
        this.mediaRepository = mediaRepository;
        this.approvalRepository = approvalRepository;
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;

        this.modelMapper.typeMap(Media.class, MediaDTO.class)
            .addMappings(m -> m.map(src -> src.getUploader().getUsername(), MediaDTO::setUploader));
    }

    public List<MediaDTO> registerMedias(List<File> mediaFiles, GalleryUploadDTO galleryUploadDTO, User user) throws IOException {
        Album album = null;
        List<Media> mediaList = new ArrayList<>();

        if (!StringUtils.isEmpty(galleryUploadDTO.getAlbumId())) {
            album = getOrRegisterAlbum(user.getUsername(), galleryUploadDTO.getAlbumId());
        }
        for (File mediaFile : mediaFiles) {
            Media media = registerMedia(mediaFile.getName(), galleryUploadDTO.getPrivacy(), mediaFile, user, album, mediaFile.length());
            checkMediaPrivacy(media, user);

            mediaList.add(media);
        }
        mediaRepository.saveAll(mediaList);
        writeMediaThumbnails(mediaFiles, user.getUsername());

        List<MediaDTO> uploadedMedias = mediaToDTOs(mediaList);
        uploadedMedias.forEach(mediaDTO -> mediaDTO.setUrl(String.format("%s/gallery/%s/%s", backendUrl, mediaDTO.getMediaType().toLowerCase(), mediaDTO.getFileName())));

        return uploadedMedias;
    }

    private void writeMediaThumbnails(List<File> medias, String user) {
        medias
            .forEach(media -> {
                try {
                    if (FileHandler.isImageFile(media.getName())) {
                        writeImageThumbnail(media, String.format("%s/thumbnail/%s/", mediaDirectory, user));
                    } else {
                        writeVideoThumbnail(media, String.format("%s/thumbnail/%s/", mediaDirectory, user));
                    }
                } catch (IOException e) {
                    logger.error("Error writing thumbnails for image {}", e.getMessage());
                }
            });
    }

    public void checkMediaPrivacy(Media media, User user) {
        if (media.getLinkStatus().equalsIgnoreCase(PUBLIC) && !CollectionUtils.containsAny(user.getAuthorities(), TRUSTED_ROLES)) {
            media.setLinkStatus(UNLISTED);
            requestPublicApproval(media, media.getName());
        }
    }

    public HashMap<String, Object> getMedias(Pageable pageable) {
        HashMap<String, Object> medias = new HashMap<>();
        medias.put("medias", mediaToDTOs(mediaRepository.getAllByOrderByIdDesc(pageable)));
        medias.put("total", mediaRepository.count());

        return medias;
    }

    public HashMap<String, Object> getPublicMedias(Pageable pageable) {
        HashMap<String, Object> publicMedias = new HashMap<>();
        publicMedias.put("medias", mediaToDTOs(mediaRepository.getAllByLinkStatusOrderByIdDesc(PUBLIC, pageable)));
        publicMedias.put("total", mediaRepository.getTotalMediaByStatus(PUBLIC));

        return publicMedias;
    }

    public HashMap<String, Object> getMediasByUser(String username, Pageable pageable) {
        HashMap<String, Object> medias = new HashMap<>();
        medias.put("medias", mediaToDTOs(mediaRepository.getAllByUploader_UsernameOrderByIdDesc(username, pageable)));
        medias.put("total", mediaRepository.getTotalMediasByUser(username));

        return medias;
    }

    public Media getMedia(int id) {
        return mediaRepository.findById(id).orElseThrow(() -> new MediaException(FILE_NOT_EXISTS_ERROR_MESSAGE));
    }

    public void saveMediaApproval(PublicMediaApproval media) {
        approvalRepository.save(media);
    }

    public Media getMediaFileName(String fileName) {
        return mediaRepository.getFirstByFileName(fileName).orElseThrow(() -> new MediaException(FILE_NOT_EXISTS_ERROR_MESSAGE));
    }

    public Media registerMedia(String mediaName, String privacy, File file, User user, Album album, Long size) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        Media media = new Media(mediaName, file.getName(), privacy, user, localDate.format(dtf), size);
        Optional.ofNullable(album).ifPresent(media::setAlbum);

        String mediaType = FileHandler.getFileMediaType(file);
        media.setMediaType(mediaType);

        return media;
    }

    public void updateMedia(GalleryUploadDTO galleryUploadDTO, int mediaID, User user) {
        Media media = getMedia(mediaID);

        if (galleryUploadDTO.getAlbumId() != null) {
            Album album = getAlbum(galleryUploadDTO.getAlbumId());
            media.setAlbum(album);
        }
        media.setName(galleryUploadDTO.getName());
        media.setLinkStatus(galleryUploadDTO.getPrivacy());

        checkMediaPrivacy(media, user);

        mediaRepository.save(media);
    }

    public PublicMediaApproval getMediaApprovalByMediaID(int mediaID) {
        return approvalRepository.getFirstByMedia_Id(mediaID).orElse(null);
    }

    public List<PublicMediaApproval> getMediaApprovalsByStatus(String status) {
        return approvalRepository.findAllByStatus(status);
    }

    public void requestPublicApproval(Media media, String mediaName) {
        PublicMediaApproval publicMediaStatus = this.getMediaApprovalByMediaID(media.getId());

        if (publicMediaStatus == null) {
            publicMediaStatus = new PublicMediaApproval(media, mediaName, "pending");
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

    public void approvePublicMedia(int mediaID) {
        PublicMediaApproval approval = getMediaApprovalByMediaID(mediaID);

        if (approval == null) {
            throw new MediaApprovalException("No media approval found for media id " + mediaID);
        }
        //Updates media with the approved public details
        Media media = approval.getMedia();
        media.setLinkStatus(PUBLIC);
        media.setName(approval.getPublicName());

        mediaRepository.save(media);
        approvalRepository.delete(approval);
    }

    public long getTotalMedias() {
        return mediaRepository.count();
    }

    public String getUserMediaDirectory(String username) {
        return String.format("%s%s/", mediaDirectory, username);
    }

    public long deleteMedia(int mediaID) throws IOException {
        Media media = getMedia(mediaID);

        File mediaFile = new File(getUserMediaDirectory(media.getUploader().getUsername()) + media.getFileName());
        long size = mediaFile.length();

        if (mediaFile.exists()) {
            Files.delete(mediaFile.toPath());
        }
        mediaRepository.delete(media);

        return size;
    }

    public Long deleteMedias(Integer[] mediaIds) {
        Long totalSize = mediaRepository.getTotalMediasSize(mediaIds);

        mediaRepository.deleteMediasByIds(mediaIds);

        return totalSize;
    }

    public void updateMediasLinkStatus(Integer[] mediaIds, String linkStatus, String uploader) {
        mediaRepository.updateMediaPrivacy(linkStatus, mediaIds, uploader);
    }

    public File getMediaFile(String mediaFileName) {
        Media media = getMediaFileName(mediaFileName);
        File file = new File(mediaDirectory + media.getUploader().getUsername() + "/" + media.getFileName());

        if (!file.exists()) {
            logger.error("Media file isn't present: {}", file.getPath());
            file = new File(mediaDirectory + "noimage.png");
        }
        return file;
    }

    public File getMediaThumbnailFile(String mediaFileName) {
        Media media = getMediaFileName(mediaFileName);
        String mediaThumbnailLocation = mediaDirectory + "thumbnail/" + media.getUploader().getUsername() + "/" + media.getFileName();

        //Gets the generated thumbnail of the video instead
        if (media.getMediaType().equalsIgnoreCase(MediaType.VIDEO.toString())) {
            mediaThumbnailLocation += ".png";
        }
        File file = new File(mediaThumbnailLocation);

        if (!file.exists()) {
            file = new File(mediaDirectory + "noimage.png");
        }
        return file;
    }

    public HashMap<String, Object> getMediaStats(String username) {
        HashMap<String, Object> stats = new HashMap<>();
        stats.put("totalMedias", mediaRepository.getTotalByUser(username));
        stats.put("recentMedias", mediaToDTOs(mediaRepository.findTop5ByUploader_UsernameOrderByIdDesc(username)));
        stats.put("totalAlbums", albumRepository.getTotalAlbumsByUser(username));

        return stats;
    }

    //Album related code
    public Album getOrRegisterAlbum(String user, String albumId) {
        return albumRepository.getFirstById(albumId).orElseGet(() -> registerNewAlbum(albumId, user));
    }

    public String generateAlbumUUID() {
        String id = RandomIDGen.getBase62(16);
        while(albumRepository.existsById(id)) {
            id = RandomIDGen.getBase62(16);
        }
        return id;
    }

    public Album registerNewAlbum(String albumName, String username) {
        String id = generateAlbumUUID();

        Album album = new Album(id, albumName, username);
        albumRepository.save(album);

        return album;
    }

    public void resetMediaAlbumIDs(Album album) {
        album.getMedias().forEach(media -> media.setAlbum(null));
        mediaRepository.saveAll(album.getMedias());
    }

    public Album getAlbum(String id) {
        return albumRepository.getFirstById(id).orElseThrow(() -> new AlbumMissingException("Album not found"));
    }

    public Album getAlbumWithMedias(String id) {
        return albumRepository.getFirstById(id).orElseThrow(() -> new AlbumMissingException("Album not found"));
    }

    public AlbumDTO getPublicAlbum(String id) {
        Album album = getAlbumWithMedias(id);

        return albumToDTO(album);
    }

    public void deleteAlbum(String albumId) {
        Album album = getAlbumWithMedias(albumId);

        resetMediaAlbumIDs(album);
        albumRepository.delete(album);
    }

    public List<AlbumDTO> getAlbumsByUser(String user) {
        List<Album> albums = albumRepository.getAllByCreator(user);

        return albumToDTOs(albums);
    }

    public long getTotalAlbums() {
        return albumRepository.count();
    }

    public void updateAlbum(String albumID, String name) {
        Album album = getAlbum(albumID);
        album.setName(name);

        albumRepository.save(album);
    }

    public void addMediasToAlbum(String albumId, int[] mediaIds, String uploader) {
        Album album = getAlbum(albumId);

        mediaRepository.setMediasAlbum(album, mediaIds, uploader);
    }

    public List<Media> getMediasByIds(Integer[] mediaIds) {
        return mediaRepository.getAllByIds(mediaIds);
    }

    //DTOs converters
    public List<AlbumDTO> albumToDTOs(List<Album> albums) {
        return albums.stream()
            .map(this::albumToDTO)
            .collect(Collectors.toList());
    }

    public AlbumDTO albumToDTO(Album album) {
        AlbumDTO albumDTO = modelMapper.map(album, AlbumDTO.class);

        if (Hibernate.isInitialized(album.getMedias())) {
            List<MediaDTO> medias = mediaToDTOs(album.getMedias());
            albumDTO.setMedias(medias);
        }
        return albumDTO;
    }

    public List<MediaDTO> mediaToDTOs(List<Media> medias) {
        return medias.stream()
            .map(this::mediaToDTO)
            .collect(Collectors.toList());
    }

    public MediaDTO mediaToDTO(Media media) {
        return modelMapper.map(media, MediaDTO.class);
    }
}
