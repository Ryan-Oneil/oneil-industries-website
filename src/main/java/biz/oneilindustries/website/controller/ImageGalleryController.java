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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageGalleryController {

    private final String galleryImagesDirectory;
    private final MediaService mediaService;
    private final AlbumService albumService;

    @Autowired
    public ImageGalleryController(MediaService mediaService, AlbumService albumService, ServletContext servletContext) {
        this.mediaService = mediaService;
        this.albumService = albumService;
        galleryImagesDirectory = servletContext.getRealPath("/WEB-INF/view/gallery/images/");
    }

    @GetMapping("gallery")
    public String showGallery(Model model) {
        model.addAttribute("media", this.mediaService.getMediaByLinkStatus("public"));

        return "gallery/index";
    }

    @GetMapping(value = "gallery/images/{mediaFileName}")
    @ResponseBody
    public byte[] getMedia(@PathVariable(value = "mediaFileName") String mediaFileName, Authentication user) throws IOException {

        File serverFile = new File(galleryImagesDirectory + mediaFileName);

        return Files.readAllBytes(serverFile.toPath());
    }

    @GetMapping(value = "gallery/images/thumbnail/{mediaFileName}")
    @ResponseBody
    public byte[] getMediaThumbnail(@PathVariable(value = "mediaFileName") String mediaFileName, Authentication user) throws IOException {

        File serverFile = new File(galleryImagesDirectory + "thumbnail/" + mediaFileName);

        return Files.readAllBytes(serverFile.toPath());
    }

    @PostMapping(value = "gallery/upload", consumes = "multipart/form-data")
    public ModelAndView uploadMedia(@ModelAttribute("GalleryUpload") @Valid GalleryUpload galleryUpload, Authentication authentication) throws IOException {

        if (galleryUpload.getFile().getSize() == 0) {
            return new ModelAndView("redirect:/gallery/upload");
        }

        String fileName = galleryUpload.getFile().getOriginalFilename();

        Media doesMediaExistsAlready = mediaService.getMediaFileName(fileName);

        if (doesMediaExistsAlready != null) {
            if (doesMediaExistsAlready.getUploader().equalsIgnoreCase(authentication.getName())) {
                return new ModelAndView("redirect:/gallery/myimages/" + fileName);
            }

            throw new MediaException(fileName + " Already exists in database");
        }

        FileHandler.writeFile(galleryUpload.getFile(), galleryImagesDirectory);

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

    @GetMapping("/gallery/delete/{mediaFileName}")
    public String deleteMedia(@PathVariable String mediaFileName, Authentication user, HttpServletRequest request) {

        Media media = mediaService.getMediaFileName(mediaFileName);

        mediaService.deleteMedia(media.getId());

        //Check redirect works
        return "redirect:/gallery/myimages";
    }

    //Getmapping ensures users are authenticated and redirects to their personal gallery
    @GetMapping("gallery/myimages")
    public ModelAndView redirectToImages(Authentication user) {
        return new ModelAndView("redirect:/gallery/" + user.getName() + "/media/");
    }

    @GetMapping("gallery/{username}/media")
    public String showUserMedias(Authentication user, @PathVariable String username, HttpServletRequest request, Model model) {

        List<Media> userMedia = mediaService.getMediasByUser(username);

        if (userMedia != null) {
            model.addAttribute("usermedias", userMedia);
        }
        return "gallery/myimages";
    }

    @GetMapping("gallery/myimages/{mediaFileName}")
    public String manageMedia(@PathVariable String mediaFileName, Authentication user, HttpServletRequest request, Model model) {
        Media media = mediaService.getMediaFileName(mediaFileName);

        model.addAttribute("media", media);

        List<Album> albums = albumService.getAlbumsByCreator(user.getName());

        model.addAttribute("albums",albums);

        Integer albumID = media.getAlbumID();


        GalleryUpload galleryUpload = new GalleryUpload(null,media.getName(),media.getLinkStatus(),null,null,null);

        if (albumID != null) {
            Album album = albumService.getAlbum(albumID);
            galleryUpload.setAlbumName(album.getName());
        }

        model.addAttribute("GalleryUpload", galleryUpload);

        return "gallery/manageimage";
    }

    @PostMapping("gallery/update/{mediaFileName}")
    public String updateMedia(@PathVariable String mediaFileName, Authentication user, HttpServletRequest request, @ModelAttribute("GalleryUpload") @Valid GalleryUpload galleryUpload)  {

        Album album = null;

        if (!galleryUpload.getAlbumName().equalsIgnoreCase("none")) {
            album = albumService.updateAlbum(galleryUpload,user.getName());
        }
        mediaService.updateMedia(galleryUpload,album,mediaFileName);

        return "redirect:/gallery/myimages/" + mediaFileName;
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

    //Redirects to logged in user's albums
    @GetMapping("/gallery/myalbums")
    public ModelAndView redirectToAlbum(Authentication user) {
        return new ModelAndView("redirect:/gallery/" + user.getName() + "/albums");
    }

    @GetMapping("/gallery/{username}/albums")
    public String showUserAlbum(Authentication user, @PathVariable String username, HttpServletRequest request, Model model) {

        List<Album> userAlbums = albumService.getAlbumsByCreator(username);
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
    public String manageMyAlbums(@PathVariable String albumName,Authentication user, HttpServletRequest request, Model model) {

        Album album = albumService.getAlbumByName(albumName);

        MediaAlbum mediaAlbum = new AlbumCreator(mediaService).createAlbum(album);

        model.addAttribute("album", mediaAlbum);

        return "/gallery/managealbum";
    }

    @PostMapping("/gallery/changealbum/{albumName}")
    public String changeAlbum(@PathVariable String albumName, Authentication user, HttpServletRequest request, @RequestParam String newAlbumName, @RequestParam boolean showUnlistedImages) {

        Album album = albumService.getAlbumByName(albumName);

        album.setName(newAlbumName);
        album.setShowUnlistedImages(showUnlistedImages);

        albumService.saveAlbum(album);

        return "redirect:/gallery/myalbums";
    }

    @PostMapping("/gallery/deletealbum/{albumName}")
    public String deleteAlbum(@PathVariable String albumName, Authentication user, HttpServletRequest request) {
        Album album = albumService.getAlbumByName(albumName);

        mediaService.resetMediaAlbumIDs(album);

        albumService.deleteAlbum(album.getId());

        return "redirect:/gallery/myalbums";
    }
}