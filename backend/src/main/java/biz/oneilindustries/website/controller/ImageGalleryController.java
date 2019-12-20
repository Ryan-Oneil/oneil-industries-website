package biz.oneilindustries.website.controller;

import static biz.oneilindustries.website.config.AppConfig.BACK_END_URL;
import static biz.oneilindustries.website.config.AppConfig.GALLERY_IMAGES_DIRECTORY;

import biz.oneilindustries.website.config.ResourceHandler;
import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.entity.Quota;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.gallery.AlbumCreator;
import biz.oneilindustries.website.gallery.MediaAlbum;
import biz.oneilindustries.website.service.AlbumService;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.GalleryUpload;
import biz.oneilindustries.website.validation.UpdatedAlbum;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/gallery")
public class ImageGalleryController {

    private final MediaService mediaService;
    private final AlbumService albumService;
    private final UserService userService;
    private final ResourceHandler handler;

    @Autowired
    public ImageGalleryController(MediaService mediaService, AlbumService albumService, UserService userService,
        ResourceHandler handler) {
        this.mediaService = mediaService;
        this.albumService = albumService;
        this.userService = userService;
        this.handler = handler;
    }

    @GetMapping("/medias")
    public List<Media> showAllMedia() {
        return mediaService.getMediaByLinkStatus("public");
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<StreamingResponseBody> streamImage(@PathVariable String imageName, Authentication user, HttpServletResponse response) {
        return displayMedia(response, imageName, GALLERY_IMAGES_DIRECTORY);
    }

    @GetMapping("/image/thumbnail/{imageName}")
    public ResponseEntity<StreamingResponseBody> streamImageThumbnail(@PathVariable String imageName, Authentication user, HttpServletResponse response) {
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
        }catch (IOException e) {
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

        return ResponseEntity.status(HttpStatus.OK).contentLength(finalServerFile.length()).cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)).body(stream);
    }

    @PostMapping("/upload")
    public String uploadMediaAPI(@RequestParam String name, @RequestParam String privacy, @RequestParam String albumName, Authentication user, HttpServletRequest request)
        throws IOException, FileUploadException {

        //Calculates the amount of storage the user can still use
        Quota userStorageLimit = userService.getQuotaByUsername(user.getName());
        long storageLeft = (userStorageLimit.getMax() * FileUtils.ONE_GB) - userStorageLimit.getUsed();

        ServletFileUpload upload = new ServletFileUpload();
        //Limits the file upload the the users remaining quota
        if (!userStorageLimit.isIgnoreQuota()) {
            upload.setSizeMax(storageLeft >= 0 ? storageLeft : 0);
        }
        FileItemIterator iterator;

        try {
            iterator = upload.getItemIterator(request);
        } catch (InvalidContentTypeException e) {
            throw new MediaException("No file uploaded");
        }
        FileItemStream item = iterator.next();
        File media = FileHandler.writeFile(item, item.getName(), GALLERY_IMAGES_DIRECTORY, user.getName());

        GalleryUpload galleryUpload = new GalleryUpload(media, name, privacy, albumName);
        Album album = null;

        if (!galleryUpload.getAlbumName().equalsIgnoreCase("none")) {
            album = albumService.registerAlbum(galleryUpload, user.getName());
        }
        mediaService.registerMedia(galleryUpload, user.getName(), album);
        userService.increaseUsedAmount(userStorageLimit, media.length());

        return BACK_END_URL + "/api/gallery/image/" + galleryUpload.getFile().getName();
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
        mediaService.deleteMedia(media.getId());

        if (quota != null && mediaSize > 0) {
            userService.decreaseUsedAmount(quota, mediaSize);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/medias/user/{username}")
    public List<Media> showUserMedias(Authentication user, @PathVariable String username, HttpServletRequest request) {
        return mediaService.getMediasByUser(username);
    }

    @PutMapping("/media/update/{mediaID}")
    public ResponseEntity updateMedia(@PathVariable int mediaID, Authentication user, HttpServletRequest request, @RequestBody @Valid GalleryUpload galleryUpload)  {

        Album album = null;

        if (!galleryUpload.getAlbumName().equalsIgnoreCase("none")) {
            album = albumService.updateAlbum(galleryUpload,user.getName());
        }
        Media media = mediaService.updateMedia(galleryUpload,album, mediaID);

        return ResponseEntity.ok(media);
    }

    @GetMapping("/album/{albumName}")
    public List<Media> showAlbum(@PathVariable String albumName) {
        Album album = albumService.getAlbumByName(albumName);

        if (album == null) {
            throw new MediaException(albumName + ": Is not a valid album");
        }
        List<Media> albumMedia = mediaService.getAlbumMedias(album.getId());

        List<Media> removedMedia = new ArrayList<>();
        for (Media media : albumMedia) {
            if (!album.isShowUnlistedImages() && media.getLinkStatus().equalsIgnoreCase("unlisted") || media.getLinkStatus().equalsIgnoreCase("private")) {
                removedMedia.add(media);
            }
        }
        albumMedia.removeAll(removedMedia);

        return albumMedia;
    }

    @GetMapping("/myalbums/{username}/names")
    public List<String> showUserAlbumNames(Authentication user, @PathVariable String username, HttpServletRequest request) {
        return albumService.getAlbumNamesByUser(username);
    }

    @GetMapping("/myalbums/{username}")
    public List<MediaAlbum> showUserAlbum(Authentication user, @PathVariable String username, HttpServletRequest request) {

        List<Album> userAlbums = albumService.getAlbumsByCreator(username);
        List<MediaAlbum> fullAlbums = new ArrayList<>();
        AlbumCreator albumCreator = new AlbumCreator(mediaService);

        for (Album album: userAlbums) {
            MediaAlbum mediaAlbum = albumCreator.createAlbum(album);
            if (mediaAlbum != null) {
                fullAlbums.add(albumCreator.createAlbum(album));
            }
        }
        return fullAlbums;
    }

    @GetMapping("/myalbum/{albumName}")
    public MediaAlbum manageMyAlbums(@PathVariable String albumName,Authentication user, HttpServletRequest request) {

        Album album = albumService.getAlbumByName(albumName);

        return new AlbumCreator(mediaService).createAlbum(album);
    }

    @PutMapping("/myalbums/update/{albumName}")
    public ResponseEntity updateAlbum(@PathVariable String albumName, Authentication user, HttpServletRequest request, @RequestBody UpdatedAlbum updatedAlbum) {

        Album album = albumService.getAlbumByName(albumName);

        album.setName(updatedAlbum.getNewAlbumName());
        album.setShowUnlistedImages(updatedAlbum.isShowUnlistedImages());

        albumService.saveAlbum(album);

        return ResponseEntity.ok(album);
    }

    @PostMapping("/myalbums/delete/{albumName}")
    public ResponseEntity deleteAlbum(@PathVariable String albumName, Authentication user, HttpServletRequest request) {
        Album album = albumService.getAlbumByName(albumName);

        mediaService.resetMediaAlbumIDs(album);
        albumService.deleteAlbum(album.getId());

        return ResponseEntity.ok(HttpStatus.OK);
    }
}