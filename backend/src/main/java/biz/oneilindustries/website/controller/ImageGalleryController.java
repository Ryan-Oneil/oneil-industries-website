package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.config.ResourceHandler;
import biz.oneilindustries.website.dto.AlbumDTO;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.PublicMediaApproval;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.service.SystemFileService;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.GalleryUpload;
import biz.oneilindustries.website.validation.UpdatedAlbum;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;
    private final ResourceHandler handler;
    private final SystemFileService fileService;

    public ImageGalleryController(MediaService mediaService, UserService userService,
        ResourceHandler handler, SystemFileService fileService) {
        this.mediaService = mediaService;
        this.userService = userService;
        this.handler = handler;
        this.fileService = fileService;
    }

    @GetMapping("/medias/{mediaType}")
    public HashMap<String, Object> showAllMedia(@PathVariable String mediaType, Pageable pageable) {
        return mediaService.getPublicMedias(pageable, mediaType);
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<StreamingResponseBody> streamImage(@PathVariable String imageName, Authentication user, HttpServletResponse response) {
        File mediaFile = mediaService.getMediaFile(imageName);

        Counter counter = Metrics.counter("request.media.view", "mediaName", mediaFile.getName(), "type", "image");
        counter.increment();

        return displayMedia(response, mediaFile);
    }

    @GetMapping("/image/thumbnail/{imageName}")
    public ResponseEntity<StreamingResponseBody> streamImageThumbnail(@PathVariable String imageName, Authentication user,
        HttpServletResponse response) {
        File mediaFile = mediaService.getMediaThumbnailFile(imageName);

        Counter counter = Metrics.counter("request.media.view", "mediaName", mediaFile.getName(), "type", "thumbnail");
        counter.increment();

        return displayMedia(response, mediaFile);
    }

    @GetMapping("/video/{videoName}")
    public void streamVideo(@PathVariable String videoName, Authentication user, HttpServletResponse response, HttpServletRequest request)
        throws ServletException {
        File serverFile = mediaService.getMediaFile(videoName);

        Counter counter = Metrics.counter("request.media.view", "mediaName", serverFile.getName(), "type", "video");
        counter.increment();

        response.setContentType("video/" + FileHandler.getContentType(serverFile.getName()));
        request.setAttribute(ResourceHandler.ATTR_FILE, serverFile);
        try {
            handler.handleRequest(request, response);
        } catch (IOException e) {
            // Client timed out or closed request
        }
    }

    private ResponseEntity<StreamingResponseBody> displayMedia(HttpServletResponse response, File mediaFile) {
        response.setContentType("image/" + FileHandler.getContentType(mediaFile.getName()));
        StreamingResponseBody stream = out -> Files.copy(mediaFile.toPath(), out);

        return ResponseEntity.status(HttpStatus.OK)
            .contentLength(mediaFile.length())
            .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
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

    @DeleteMapping("/media/delete/{mediaId}")
    public ResponseEntity<HttpStatus> deleteMedia(@PathVariable int mediaId, Authentication user, HttpServletRequest request) throws IOException {
        long mediaSize = mediaService.deleteMedia(mediaId);
        userService.decreaseUsedAmount(user.getName(), mediaSize);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/user/medias/{username}/{mediaType}")
    public HashMap<String, Object> showUserMedias(Authentication user, @PathVariable String username, HttpServletRequest request, Pageable pageable,
        @PathVariable String mediaType) {
        return mediaService.getMediasByUser(username, pageable, mediaType);
    }

    @GetMapping("/user/{username}/stats")
    public HashMap<String, Object> showUserMediaStats(Authentication user, @PathVariable String username, HttpServletRequest request) {
        return mediaService.getMediaStats(username);
    }

    @PutMapping("/media/update/{mediaID}")
    public ResponseEntity<HttpStatus> updateMedia(@PathVariable int mediaID, Authentication user, HttpServletRequest request,
        @RequestBody @Valid GalleryUpload galleryUpload) {
        User userAuth = (User) user.getPrincipal();

        mediaService.updateMedia(galleryUpload, mediaID, userAuth);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/album/{albumID}")
    public AlbumDTO showAlbum(@PathVariable String albumID) {
        return mediaService.getPublicAlbum(albumID);
    }

    @GetMapping("/myalbums/{username}")
    public List<AlbumDTO> showUserAlbum(Authentication user, @PathVariable String username, HttpServletRequest request) {
        return mediaService.getAlbumsByUser(username);
    }

    @GetMapping("/myalbum/{albumID}")
    public Album manageMyAlbums(@PathVariable String albumID, Authentication user, HttpServletRequest request) {
        return mediaService.getAlbumWithMedias(albumID);
    }

    @PutMapping("/myalbums/update/{albumID}")
    public ResponseEntity<HttpStatus> updateAlbum(@PathVariable String albumID, Authentication user, HttpServletRequest request, UpdatedAlbum updatedAlbum) {
        mediaService.updateAlbum(albumID, updatedAlbum.getNewAlbumName(), updatedAlbum.isShowUnlistedImages());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/myalbums/delete/{albumID}")
    public ResponseEntity<HttpStatus> deleteAlbum(@PathVariable String albumID, Authentication user, HttpServletRequest request) {
        mediaService.deleteAlbum(albumID);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    // Admin related APIs

    @GetMapping("/admin/medias/{mediaType}")
    public ResponseEntity<HashMap<String, Object>> getAllMedias(@PathVariable String mediaType, Pageable pageable) {
        return  ResponseEntity.ok(mediaService.getMedias(mediaType, pageable));
    }

    @GetMapping("/admin/media/pendingApproval")
    public ResponseEntity<List<PublicMediaApproval>> getMediasRequiringApproval() {
        return ResponseEntity.ok(mediaService.getMediaApprovalsByStatus("pending"));
    }

    @PutMapping("/admin/media/{approvalMediaID}/approve")
    public ResponseEntity<HttpStatus> approveMediaApproval(@PathVariable int approvalMediaID) {
        mediaService.approvePublicMedia(approvalMediaID);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/admin/media/{approvalMediaID}/deny")
    public ResponseEntity<HttpStatus> denyMediaApproval(@PathVariable int approvalMediaID) {
        mediaService.setMediaApprovalStatus(approvalMediaID, "denied");

        return ResponseEntity.ok(HttpStatus.OK);
    }
}