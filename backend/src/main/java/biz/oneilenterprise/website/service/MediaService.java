package biz.oneilenterprise.website.service;

import static biz.oneilenterprise.website.security.SecurityConstants.TRUSTED_ROLES;
import static biz.oneilenterprise.website.utils.FileHandlerUtil.writeImageThumbnail;
import static biz.oneilenterprise.website.utils.FileHandlerUtil.writeVideoThumbnail;

import biz.oneilenterprise.website.dto.AlbumDTO;
import biz.oneilenterprise.website.dto.MediaDTO;
import biz.oneilenterprise.website.dto.MediaUploadDTO;
import biz.oneilenterprise.website.entity.Album;
import biz.oneilenterprise.website.entity.Media;
import biz.oneilenterprise.website.entity.PublicMediaApproval;
import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.enums.MediaType;
import biz.oneilenterprise.website.enums.PrivacyStatus;
import biz.oneilenterprise.website.exception.AlbumMissingException;
import biz.oneilenterprise.website.exception.MediaApprovalException;
import biz.oneilenterprise.website.exception.MediaException;
import biz.oneilenterprise.website.repository.AlbumRepository;
import biz.oneilenterprise.website.repository.MediaRepository;
import biz.oneilenterprise.website.utils.FileHandlerUtil;
import biz.oneilenterprise.website.utils.RandomIDGenUtil;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.util.ObjectUtils;

@Service
public class MediaService {

    private static final Logger logger = LogManager.getLogger(MediaService.class);

    private static final String FILE_NOT_EXISTS_ERROR_MESSAGE = "Media does not exist on this server";

    private final MediaRepository mediaRepository;
    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;

    @Value("${service.media.location}")
    private String mediaDirectory;

    @Value("${service.backendUrl}")
    private String backendUrl;

    public MediaService(MediaRepository mediaRepository, AlbumRepository albumRepository, ModelMapper modelMapper) {
        this.mediaRepository = mediaRepository;
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.typeMap(Media.class, MediaDTO.class)
            .addMappings(m -> m.map(src -> src.getUploader().getUsername(), MediaDTO::setUploader));
    }

    public List<MediaDTO> registerMedias(List<File> mediaFiles, MediaUploadDTO mediaUploadDTO, User user) {
        Album album = null;

        if (!ObjectUtils.isEmpty(mediaUploadDTO.getAlbumId())) {
            album = getOrRegisterAlbum(user.getUsername(), mediaUploadDTO.getAlbumId());
        }
        Album finalAlbum = album;
        List<Media> mediaList = mediaFiles.stream()
            .map(file -> registerMedia(file.getName(), PrivacyStatus.UNLISTED.toString().toLowerCase(), file, user, finalAlbum, file.length()))
            .collect(Collectors.toList());

        mediaRepository.saveAll(mediaList);
        writeMediaThumbnails(mediaFiles, user.getUsername());

        List<MediaDTO> uploadedMedias = mediaToDTOs(mediaList);
        uploadedMedias.forEach(mediaDTO -> mediaDTO.setUrl(String.format("%s/gallery/%s/%s", backendUrl, mediaDTO.getMediaType().toLowerCase(), mediaDTO.getFileName())));

        return uploadedMedias;
    }

    public void writeMediaThumbnails(List<File> medias, String user) {
        medias
            .forEach(media -> {
                try {
                    if (FileHandlerUtil.isImageFile(media.getName())) {
                        writeImageThumbnail(media, String.format("%s/thumbnail/%s/", mediaDirectory, user));
                    } else {
                        writeVideoThumbnail(media, String.format("%s/thumbnail/%s/", mediaDirectory, user));
                    }
                } catch (IOException e) {
                    logger.error("Error writing thumbnails for image {}", e.getMessage());
                }
            });
    }

    public HashMap<String, Object> getMedias(Pageable pageable) {
        HashMap<String, Object> medias = new HashMap<>();
        medias.put("medias", mediaToDTOs(mediaRepository.getAllByOrderByIdDesc(pageable)));
        medias.put("total", mediaRepository.count());

        return medias;
    }

    public HashMap<String, Object> getPublicMedias(Pageable pageable) {
        HashMap<String, Object> publicMedias = new HashMap<>();
        publicMedias.put("medias", mediaToDTOs(mediaRepository.getAllByLinkStatusOrderByIdDesc(PrivacyStatus.PUBLIC.toString().toLowerCase(), pageable)));
        publicMedias.put("total", mediaRepository.getTotalMediaByStatus(PrivacyStatus.PUBLIC.toString().toLowerCase()));

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

    public Media getMediaFileName(String fileName) {
        return mediaRepository.getFirstByFileName(fileName).orElseThrow(() -> new MediaException(FILE_NOT_EXISTS_ERROR_MESSAGE));
    }

    public Media registerMedia(String mediaName, String privacy, File file, User user, Album album, Long size) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        Media media = new Media(mediaName, file.getName(), privacy, user, localDate.format(dtf), size);
        Optional.ofNullable(album).ifPresent(media::setAlbum);

        String mediaType = FileHandlerUtil.getFileMediaType(file);
        media.setMediaType(mediaType);

        return media;
    }

    public void updateMedia(MediaUploadDTO mediaUploadDTO, int mediaID) {
        Media media = getMedia(mediaID);

        if (mediaUploadDTO.getAlbumId() != null) {
            Album album = getAlbum(mediaUploadDTO.getAlbumId());
            media.setAlbum(album);
        }
        media.setName(mediaUploadDTO.getName());
        mediaRepository.save(media);
    }

    public Media getMediaApprovalByMediaID(int mediaID) {
        Media media = getMedia(mediaID);

        if (media.getPublicMediaApproval() == null) {
            throw new MediaApprovalException("Media approval item not found");
        }
        return media;
    }

    public List<MediaDTO> getMediaApprovalsByStatus(String status) {
        return mediaToDTOs(mediaRepository.getAllByPublicMediaApproval_StatusOrderByIdDesc(status));
    }

    public PublicMediaApproval requestPublicApproval(Media media) {
        PublicMediaApproval publicMediaStatus = media.getPublicMediaApproval();

        if (publicMediaStatus == null) {
            publicMediaStatus = new PublicMediaApproval(media, "pending");

            return publicMediaStatus;
        } else {
            throw new MediaApprovalException("This media has previously requested public access. Approval status: " + publicMediaStatus
                .getStatus());
        }
    }

    public void massRequestPublicApproval(Integer[] mediaIds) {
        List<Media> medias = getMediasByIds(mediaIds);

        medias.forEach(media -> media.setPublicMediaApproval(requestPublicApproval(media)));

        mediaRepository.saveAll(medias);
    }

    public void setMediaApprovalStatus(int mediaApprovalID, String newStatus) {
        Media media = getMediaApprovalByMediaID(mediaApprovalID);

        media.getPublicMediaApproval().setStatus(newStatus);
        mediaRepository.save(media);
    }

    public void approvePublicMedia(int mediaID) {
        Media media = getMediaApprovalByMediaID(mediaID);
        media.setLinkStatus(PrivacyStatus.PUBLIC.toString().toLowerCase());
        media.setPublicMediaApproval(null);

        mediaRepository.save(media);
    }

    public long getTotalMedias() {
        return mediaRepository.count();
    }

    public String getUserMediaDirectory(String username) {
        return String.format("%s%s/", mediaDirectory, username);
    }

    public Long deleteMedias(Integer[] mediaIds) {
        Long totalSize = mediaRepository.getTotalMediasSize(mediaIds);
        List<Media> mediaList = mediaRepository.getAllByIds(mediaIds);

        mediaRepository.deleteMediasByIds(mediaIds);
        mediaList.forEach(media -> FileHandlerUtil.deleteFile(getUserMediaDirectory(media.getUploader().getUsername()) + media.getFileName()));

        return totalSize;
    }

    public String updateMediasLinkStatus(Integer[] mediaIds, String status, User uploader) {
        String linkStatus = PrivacyStatus.valueOf(status.toUpperCase()).toString();

        if (linkStatus.equalsIgnoreCase(PrivacyStatus.PUBLIC.toString()) && !CollectionUtils.containsAny(uploader.getAuthorities(), TRUSTED_ROLES)) {
            massRequestPublicApproval(mediaIds);
            return "Requested approval to make media " + PrivacyStatus.PUBLIC.toString().toLowerCase();
        }
        mediaRepository.updateMediaPrivacy(linkStatus, mediaIds, uploader.getUsername());

        return "";
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
        String id = RandomIDGenUtil.getBase62(16);
        while(albumRepository.existsById(id)) {
            id = RandomIDGenUtil.getBase62(16);
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
