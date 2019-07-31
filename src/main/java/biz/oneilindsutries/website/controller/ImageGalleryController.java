package biz.oneilindsutries.website.controller;

import biz.oneilindsutries.website.entity.Album;
import biz.oneilindsutries.website.entity.Media;
import biz.oneilindsutries.website.exception.FileExistsException;
import biz.oneilindsutries.website.exception.NotAuthorisedException;
import biz.oneilindsutries.website.filecreater.FileHandler;
import biz.oneilindsutries.website.gallery.AlbumCreator;
import biz.oneilindsutries.website.gallery.MediaAlbum;
import biz.oneilindsutries.website.service.AlbumService;
import biz.oneilindsutries.website.service.MediaService;
import biz.oneilindsutries.website.validation.GalleryUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageGalleryController {

    private static final String GALLERY_IMAGES_DIRECTORY = "C:\\Users\\Ryan\\Desktop\\OneilIndustries\\src\\main\\webapp\\WEB-INF\\view\\gallery\\images\\";
    private static final String FILE_NOT_EXISTS_ERROR_MESSAGE = ": Does not exist on this server";
    private static final String USER_NOT_AUTHORISED_MESSAGE = " is not authorised";

    private final MediaService mediaService;
    private final AlbumService albumService;

    @Autowired
    public ImageGalleryController(MediaService mediaService, AlbumService albumService) {
        this.mediaService = mediaService;
        this.albumService = albumService;
    }

    @GetMapping("gallery")
    public String showGallery(Model model) {
        model.addAttribute("media", this.mediaService.getMediaByLinkStatus("public"));

        return "gallery/index";
    }

    @GetMapping(value = "gallery/images/{mediaFileName}")
    @ResponseBody
    public byte[] getMedia(@PathVariable(value = "mediaFileName") String mediaFileName, Authentication user) throws IOException, NotAuthorisedException {
        Media media = mediaService.getMediaFileName(mediaFileName);

        File serverFile = new File(GALLERY_IMAGES_DIRECTORY + mediaFileName);

        if (!serverFile.exists() || !serverFile.isFile()) {
            throw new FileNotFoundException(mediaFileName + FILE_NOT_EXISTS_ERROR_MESSAGE);
        }

        if (media.getLinkStatus().equalsIgnoreCase("private") && ( user == null || !user.getName().equalsIgnoreCase(media.getUploader())) ) {
            throw new NotAuthorisedException("You do not have the permission to view this media");
        }

        return Files.readAllBytes(serverFile.toPath());
    }

    @GetMapping(value = "gallery/images/thumbnail/{mediaFileName}")
    @ResponseBody
    public byte[] getMediaThumbnail(@PathVariable(value = "mediaFileName") String mediaFileName, Authentication user) throws IOException, NotAuthorisedException {
        Media media = mediaService.getMediaFileName(mediaFileName);

        File serverFile = new File(GALLERY_IMAGES_DIRECTORY + "thumbnail/" + mediaFileName);

        if (!serverFile.exists() || !serverFile.isFile()) {
            throw new FileNotFoundException(mediaFileName + FILE_NOT_EXISTS_ERROR_MESSAGE);
        }

        if (media.getLinkStatus().equalsIgnoreCase("private") && (user == null || !user.getName().equalsIgnoreCase(media.getUploader())) ) {
            throw new NotAuthorisedException("You do not have the permission to view this media");
        }
        return Files.readAllBytes(serverFile.toPath());
    }

    @PostMapping(value = "gallery/upload", consumes = "multipart/form-data")
    public ModelAndView uploadMedia(@ModelAttribute("GalleryUpload") @Valid GalleryUpload galleryUpload, Authentication authentication) throws FileExistsException, IOException {

        if (galleryUpload.getFile().getSize() == 0) {
            return new ModelAndView("redirect:/gallery/upload");
        }

        String fileName = galleryUpload.getFile().getOriginalFilename();

        Media doesMediaExistsAlready = mediaService.getMediaFileName(fileName);

        if (doesMediaExistsAlready != null) {
            throw new FileExistsException(fileName + " Already exists in database");
        }

        FileHandler.writeFile(galleryUpload.getFile(),GALLERY_IMAGES_DIRECTORY);

        Album album = null;

        if (!galleryUpload.getAlbumName().equalsIgnoreCase("none")) {
            album = albumService.registerAlbum(galleryUpload,authentication.getName());
        }
        mediaService.registerMedia(galleryUpload,authentication.getName(),album);

        return new ModelAndView("redirect:/gallery/myimages/" + fileName);
    }

    @GetMapping("gallery/upload")
    public String showUpload(Model model, Authentication user) {

        List<Album> albums = albumService.getAlbumsByCreator(user.getName());

        model.addAttribute("GalleryUpload",new GalleryUpload());
        model.addAttribute("albums",albums);

        return "gallery/upload";
    }

    @GetMapping("/gallery/delete/{mediaID}")
    public String deleteMedia(@PathVariable int mediaID, Authentication user) throws FileNotFoundException, NotAuthorisedException {

        Media media = mediaService.getMedia(mediaID);

        if (media == null) {
            throw new FileNotFoundException("Media doesn't exist");
        }
        if (user == null || !media.getUploader().equalsIgnoreCase(user.getName())) {
            throw new NotAuthorisedException("You cannot delete a media you don't own");
        }
        mediaService.deleteMedia(media.getId());

        //Check redirect works
        return "redirect:/gallery/myimages";
    }

    @GetMapping("gallery/myimages")
    public String showUserMedias(Model model, Authentication user) {

        List<Media> userMedia = mediaService.getMediasByUser(user.getName());

        if (userMedia != null) {
            model.addAttribute("usermedias", userMedia);
        }
        return "gallery/myimages";
    }

    @GetMapping("gallery/myimages/{mediaFileName}")
    public String manageMedia(@PathVariable(value = "mediaFileName") String mediaFileName, Model model, Authentication user) throws FileNotFoundException, NotAuthorisedException {
        Media media = mediaService.getMediaFileName(mediaFileName);

        if (media == null) {
            throw new FileNotFoundException(mediaFileName + FILE_NOT_EXISTS_ERROR_MESSAGE);
        }
        if (!media.getUploader().equals(user.getName())) {
            throw new NotAuthorisedException(user.getName() + USER_NOT_AUTHORISED_MESSAGE);
        }

        model.addAttribute("media", media);

        List<Album> albums = albumService.getAlbumsByCreator(user.getName());

        model.addAttribute("albums",albums);
        model.addAttribute("GalleryUpload",new GalleryUpload());

        return "gallery/manageimage";
    }

    @PostMapping("gallery/update/{mediaID}")
    public String updateMedia(@PathVariable(value = "mediaID") String id, @ModelAttribute("GalleryUpload") @Valid GalleryUpload galleryUpload, Authentication user) throws FileNotFoundException, NotAuthorisedException, FileExistsException {
        Media media = mediaService.getMedia(Integer.valueOf(id));

        if (media == null) {
            throw new FileNotFoundException("Updating media wasn't found");
        }
        if (!media.getUploader().equals(user.getName())) {
            throw new NotAuthorisedException(user.getName() + USER_NOT_AUTHORISED_MESSAGE);
        }

        media.setName(galleryUpload.getName());
        media.setLinkStatus(galleryUpload.getPrivacy());

        if (!galleryUpload.getAlbumName().equalsIgnoreCase("none")) {
            Album album = albumService.getAlbumByName(galleryUpload.getAlbumName());

            if (album == null) {
                if (albumService.getAlbumByName(galleryUpload.getNewalbumName()) != null) {
                    throw new FileExistsException("This album already exists");
                }
                album = new Album(galleryUpload.getNewalbumName(),user.getName(),galleryUpload.getShowUnlistedImages());
            }
            albumService.saveAlbum(album);
            media.setAlbumID(album.getId());
        }else {
            media.setAlbumID(null);
        }
        mediaService.saveMedia(media);

        return "redirect:/gallery/myimages/" + media.getFileName();
    }

    @GetMapping("/gallery/album/{albumName}")
    public String showAlbum(@PathVariable String albumName, Model model) throws FileNotFoundException {
        Album album = albumService.getAlbumByName(albumName);

        if (album == null) {
            throw new FileNotFoundException(albumName + ": Is not a valid album name");
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

        return "gallery/album";
    }

    @GetMapping("/gallery/myalbums")
    public String showMyAlbums(Authentication user, Model model) {

        List<Album> userAlbums = albumService.getAlbumsByCreator(user.getName());
        List<MediaAlbum> fullAlbums = new ArrayList<>();
        AlbumCreator albumCreator = new AlbumCreator(mediaService);

        for (Album album: userAlbums) {
            MediaAlbum mediaAlbum = albumCreator.createAlbum(album);
            if (mediaAlbum != null) {
                fullAlbums.add(albumCreator.createAlbum(album));
            }
        }
        model.addAttribute("albums",fullAlbums);

        return "gallery/myalbums";
    }

    @GetMapping("/gallery/managealbum/{albumName}")
    public String manageMyAlbums(@PathVariable String albumName,Authentication user, Model model) throws FileNotFoundException, NotAuthorisedException {

        Album album = albumService.getAlbumByName(albumName);

        if (album == null) {
            throw new FileNotFoundException("This album doesn't exist");
        }

        if (!album.getCreator().equalsIgnoreCase(user.getName())) {
            throw new NotAuthorisedException("You cannot manage a album you didn't create");
        }

        MediaAlbum mediaAlbum = new AlbumCreator(mediaService).createAlbum(album);

        model.addAttribute("album", mediaAlbum);

        return "/gallery/managealbum";
    }

    @PostMapping("/gallery/changealbum/{albumName}")
    public String changeAlbum(@PathVariable String albumName, @RequestParam String newAlbumName, @RequestParam boolean showUnlistedImages, Authentication user) throws FileNotFoundException, NotAuthorisedException {

        Album album = albumService.getAlbumByName(albumName);

        if (album == null) {
            throw new FileNotFoundException("Album not found");
        }
        if (!album.getCreator().equalsIgnoreCase(user.getName())) {
            throw new NotAuthorisedException("You cannot manage a album you didn't create");
        }

        album.setName(newAlbumName);
        album.setShowUnlistedImages(showUnlistedImages);

        albumService.saveAlbum(album);

        return "redirect:/gallery/myalbums";
    }

    @PostMapping("/gallery/deletealbum/{albumName}")
    public String deleteAlbum(@PathVariable String albumName, Authentication user) throws FileNotFoundException, NotAuthorisedException {
        Album album = albumService.getAlbumByName(albumName);

        if (album == null) {
            throw new FileNotFoundException("Album not found");
        }
        if (!album.getCreator().equalsIgnoreCase(user.getName())) {
            throw new NotAuthorisedException("You cannot delete a album you didn't create");
        }

        List<Media> albumMedia = mediaService.getAlbumMedias(album.getId());

        for (Media media : albumMedia) {
            media.setAlbumID(null);
            mediaService.saveMedia(media);
        }
        albumService.deleteAlbum(album.getId());

        return "redirect:/gallery/myalbums";
    }
}