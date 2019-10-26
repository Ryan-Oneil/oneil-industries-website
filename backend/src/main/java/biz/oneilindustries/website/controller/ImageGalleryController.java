package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.gallery.AlbumCreator;
import biz.oneilindustries.website.gallery.MediaAlbum;
import biz.oneilindustries.website.service.AlbumService;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.validation.GalleryUpload;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gallery")
public class ImageGalleryController {

    private static final String GALLERY_IMAGES_DIRECTORY = "E:/images/";
    private final MediaService mediaService;
    private final AlbumService albumService;

    @Autowired
    public ImageGalleryController(MediaService mediaService, AlbumService albumService) {
        this.mediaService = mediaService;
        this.albumService = albumService;
    }

    @GetMapping("/medias")
    public List<Media> showAllMedia() {
        return mediaService.getMediaByLinkStatus("public");
    }

    @GetMapping(value = "/media/{mediaID}")
    public void getMedia(@PathVariable("mediaID") int mediaID, Authentication user, HttpServletResponse response) throws IOException {
        displayMedia(response, mediaID, GALLERY_IMAGES_DIRECTORY);
    }

    @GetMapping(value = "/media/thumbnail/{mediaID}")
    public void getMediaThumbnail(@PathVariable("mediaID") int mediaID, Authentication user, HttpServletResponse response) throws IOException {
        displayMedia(response, mediaID, GALLERY_IMAGES_DIRECTORY + "thumbnail/");
    }

    private void displayMedia(HttpServletResponse response, int mediaID, String directory) throws IOException {

        Media media = mediaService.getMedia(mediaID);

        String imageName = media.getFileName();

        File serverFile = new File(directory + media.getUploader() + "/" + imageName);

        if (!serverFile.exists()) {
            serverFile = new File(directory + "noimage.png");
        }

        if (media.getMediaType().equalsIgnoreCase("image")) {
            response.setContentType("image/" + FileHandler.getContentType(media.getFileName()));
        } else {
            response.setContentType("video/" + FileHandler.getContentType(media.getFileName()));
        }

        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(Files.readAllBytes(serverFile.toPath()));
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @PostMapping("/upload")
    public String uploadMediaAPI(@Valid GalleryUpload galleryUpload, Authentication user, HttpServletRequest request) throws IOException {

        FileHandler.writeFile(galleryUpload.getFile(), GALLERY_IMAGES_DIRECTORY, user.getName());

        Album album = null;

        if (!galleryUpload.getAlbumName().equalsIgnoreCase("none")) {
            album = albumService.registerAlbum(galleryUpload, user.getName());
        }
        mediaService.registerMedia(galleryUpload, user.getName(), album);

        return request.getLocalName() + "/gallery/images/" + galleryUpload.getFile().getOriginalFilename();
    }

    @DeleteMapping("/delete/{mediaFileName}")
    public ResponseEntity deleteMedia(@PathVariable String mediaFileName, Authentication user) {

        Media media = mediaService.getMediaFileName(mediaFileName);

        mediaService.deleteMedia(media.getId());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/medias/user/{username}")
    public List<Media> showUserMedias(Authentication user, @PathVariable String username, HttpServletRequest request) {

        return mediaService.getMediasByUser(username);
    }

    @PostMapping("/update/{mediaFileName}")
    public ResponseEntity updateMedia(@PathVariable String mediaFileName, Authentication user, HttpServletRequest request, @ModelAttribute("GalleryUpload") @Valid GalleryUpload galleryUpload)  {

        Album album = null;

        if (!galleryUpload.getAlbumName().equalsIgnoreCase("none")) {
            album = albumService.updateAlbum(galleryUpload,user.getName());
        }
        mediaService.updateMedia(galleryUpload,album,mediaFileName);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/album/{albumName}")
    public List<Media> showAlbum(@PathVariable String albumName, Model model) throws FileNotFoundException {
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

        model.addAttribute("albumMedias", albumMedia);
        model.addAttribute("album", album);

        return albumMedia;
    }

    @GetMapping("/myalbums/{username}/names")
    public List<String> showUserAlbumNames(Authentication user, @PathVariable String username, HttpServletRequest request) {
        return albumService.getAlbumNamesByUser(user.getName());
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

    @PostMapping("/myalbums/update/{albumName}")
    public ResponseEntity updateAlbum(@PathVariable String albumName, Authentication user, HttpServletRequest request, @RequestParam String newAlbumName, @RequestParam boolean showUnlistedImages) {

        Album album = albumService.getAlbumByName(albumName);

        album.setName(newAlbumName);
        album.setShowUnlistedImages(showUnlistedImages);

        albumService.saveAlbum(album);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/myalbums/delete/{albumName}")
    public ResponseEntity deleteAlbum(@PathVariable String albumName, Authentication user, HttpServletRequest request) {
        Album album = albumService.getAlbumByName(albumName);

        mediaService.resetMediaAlbumIDs(album);

        albumService.deleteAlbum(album.getId());

        return ResponseEntity.ok(HttpStatus.OK);
    }
}