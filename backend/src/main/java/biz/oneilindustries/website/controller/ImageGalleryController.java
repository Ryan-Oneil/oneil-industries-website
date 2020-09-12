package biz.oneilindustries.website.controller;

import static biz.oneilindustries.AppConfig.GALLERY_IMAGES_DIRECTORY;
import static biz.oneilindustries.website.security.SecurityConstants.TRUSTED_ROLES;

import biz.oneilindustries.website.config.ResourceHandler;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.entity.Quota;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.pojo.AlbumDetails;
import biz.oneilindustries.website.service.AlbumService;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.service.SystemFileService;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.GalleryUpload;
import biz.oneilindustries.website.validation.UpdatedAlbum;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/gallery")
public class ImageGalleryController {

    private final MediaService mediaService;
    private final AlbumService albumService;
    private final UserService userService;
    private final ResourceHandler handler;
    private final SystemFileService fileService;

    private static final String PUBLIC = "public";
    private static final String UNLISTED = "unlisted";

    @Autowired
    public ImageGalleryController(MediaService mediaService, AlbumService albumService, UserService userService,
        ResourceHandler handler, SystemFileService fileService) {
        this.mediaService = mediaService;
        this.albumService = albumService;
        this.userService = userService;
        this.handler = handler;
        this.fileService = fileService;
    }

    @GetMapping("/medias")
    public HashMap<String, Object> showAllMedia(Pageable pageable) {
        return mediaService.getPublicMedias(pageable);
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<StreamingResponseBody> streamImage(@PathVariable String imageName, Authentication user, HttpServletResponse response) {
        return displayMedia(response, imageName, GALLERY_IMAGES_DIRECTORY);
    }

    @GetMapping("/image/thumbnail/{imageName}")
    public ResponseEntity<StreamingResponseBody> streamImageThumbnail(@PathVariable String imageName, Authentication user,
        HttpServletResponse response) {
        return displayMedia(response, imageName, GALLERY_IMAGES_DIRECTORY + "thumbnail/");
    }

    @GetMapping("/video/{videoName}")
    public void streamVideo(@PathVariable String videoName, Authentication user, HttpServletResponse response, HttpServletRequest request)
        throws ServletException {

        Media media = mediaService.getMediaFileName(videoName);

        File serverFile = new File(GALLERY_IMAGES_DIRECTORY + media.getUploader() + "/" + media.getFileName());

        response.setContentType("video/" + FileHandler.getContentType(media.getFileName()));
        request.setAttribute(ResourceHandler.ATTR_FILE, serverFile);
        try {
            handler.handleRequest(request, response);
        } catch (IOException e) {
            // Client timed out or closed request
        }
    }

    private ResponseEntity<StreamingResponseBody> displayMedia(HttpServletResponse response, String imageName, String directory) {

        Media media = mediaService.getMediaFileName(imageName);

        File serverFile = new File(directory + media.getUploader() + "/" + media.getFileName());

        if (!serverFile.exists()) {
            serverFile = new File(directory + "noimage.png");
        }
        response.setContentType("image/" + FileHandler.getContentType(media.getFileName()));

        File finalServerFile = serverFile;
        StreamingResponseBody stream = out -> Files.copy(finalServerFile.toPath(), out);

        return ResponseEntity.status(HttpStatus.OK).contentLength(finalServerFile.length()).cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
            .body(stream);
    }

    @PostMapping("/upload")
    public String uploadMediaAPI(GalleryUpload galleryUpload, Authentication user, HttpServletRequest request) throws IOException, FileUploadException {
        User userAuth = (User) user.getPrincipal();
        long remainingQuota = userService.getRemainingQuota(userAuth.getUsername());

        List<File> uploadedFiles = fileService
            .handleFileUpload(request, remainingQuota, mediaService.getUserMediaDirectory(userAuth.getUsername()), true);

        long sizeOfFiles = uploadedFiles.stream().mapToLong(File::length).sum();
        userService.increaseUsedAmount(userAuth.getUsername(), sizeOfFiles);

        return mediaService.registerMedias(uploadedFiles, galleryUpload, userAuth);
    }

    @DeleteMapping("/media/delete/{mediaInt}")
    public ResponseEntity deleteMedia(@PathVariable int mediaInt, Authentication user, HttpServletRequest request) throws IOException {

        Media media = mediaService.getMedia(mediaInt);
        Quota quota = userService.getQuotaByUsername(user.getName());

        File mediaFile = new File(GALLERY_IMAGES_DIRECTORY + media.getUploader() + "/" + media.getFileName());
        long mediaSize = 0;

        if (mediaFile.exists()) {
            mediaSize = mediaFile.length();
            Files.delete(mediaFile.toPath());
        }

        if (media.getPublicMediaApproval() != null) {
            mediaService.deleteMediaApproval(media.getPublicMediaApproval().getId());
        }
        mediaService.deleteMedia(media.getId());
        userService.decreaseUsedAmount(quota, mediaSize);

        if (media.getAlbum() != null) {
            albumService.deleteAlbumIfEmpty(media.getAlbum().getId());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/medias/user/{username}")
    public HashMap<String, Object> showUserMedias(Authentication user, @PathVariable String username, HttpServletRequest request, Pageable pageable) {
        return mediaService.getMediasByUser(username, pageable);
    }

    @PutMapping("/media/update/{mediaID}")
    public ResponseEntity updateMedia(@PathVariable int mediaID, Authentication user, HttpServletRequest request,
        @RequestBody @Valid GalleryUpload galleryUpload) {
        Album album = null;

        if (galleryUpload.getAlbum() != null) {
            album = albumService.getOrRegisterAlbum(galleryUpload.getAlbum(), galleryUpload.getShowUnlistedImages(), user.getName());
        }

        if (galleryUpload.getPrivacy().equalsIgnoreCase(PUBLIC) && !CollectionUtils.containsAny(user.getAuthorities(), TRUSTED_ROLES)) {
            galleryUpload.setPrivacy(UNLISTED);
            mediaService.requestPublicApproval(mediaID, galleryUpload.getName(), album);
            return ResponseEntity.ok("Adding or updating a public media will require admin approval. Once approved your media changes will be live");
        }
        mediaService.updateMedia(galleryUpload, album, mediaID);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/album/{albumID}")
    public Album showAlbum(@PathVariable String albumID) {
        Album album = albumService.getAlbumWithMediaByID(albumID);

        if (album == null) {
            throw new MediaException(albumID + ": Is not a valid album");
        }

        if (!album.isShowUnlistedImages()) {
            //Removes all medias that aren't set to public
            List<Media> publicMedias = album.getMedias().stream().filter(media -> media.getLinkStatus().equalsIgnoreCase(PUBLIC))
                .collect(Collectors.toList());
            album.setMedias(publicMedias);
        }
        return album;
    }

    @GetMapping("/myalbums/{username}/details")
    public List<AlbumDetails> showUserAlbumNames(Authentication user, @PathVariable String username, HttpServletRequest request) {
        return albumService.getAlbumDetailsByCreator(username);
    }

    @GetMapping("/myalbums/{username}")
    public List<Album> showUserAlbum(Authentication user, @PathVariable String username, HttpServletRequest request) {
        return albumService.getAlbumsWithMediaByCreator(username);
    }

    @GetMapping("/myalbum/{albumID}")
    public Album manageMyAlbums(@PathVariable String albumID, Authentication user, HttpServletRequest request) {
        return albumService.getAlbumWithMediaByID(albumID);
    }

    @PutMapping("/myalbums/update/{albumID}")
    public ResponseEntity updateAlbum(@PathVariable String albumID, Authentication user, HttpServletRequest request, UpdatedAlbum updatedAlbum) {
        albumService.updateAlbum(albumID, updatedAlbum.getNewAlbumName(), updatedAlbum.isShowUnlistedImages());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/myalbums/delete/{albumID}")
    public ResponseEntity deleteAlbum(@PathVariable String albumID, Authentication user, HttpServletRequest request) {
        Album album = albumService.getAlbumWithMediaByID(albumID);

        mediaService.resetMediaAlbumIDs(album);
        albumService.deleteAlbum(albumID);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}