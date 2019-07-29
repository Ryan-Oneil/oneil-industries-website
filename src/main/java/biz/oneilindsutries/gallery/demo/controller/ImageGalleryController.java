package biz.oneilindsutries.gallery.demo.controller;

import biz.oneilindsutries.gallery.demo.entity.Album;
import biz.oneilindsutries.gallery.demo.entity.Image;
import biz.oneilindsutries.gallery.demo.exception.FileExistsException;
import biz.oneilindsutries.gallery.demo.exception.NotAuthorisedException;
import biz.oneilindsutries.gallery.demo.filecreater.FileHandler;
import biz.oneilindsutries.gallery.demo.gallery.AlbumCreator;
import biz.oneilindsutries.gallery.demo.gallery.ImageAlbum;
import biz.oneilindsutries.gallery.demo.service.AlbumService;
import biz.oneilindsutries.gallery.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageGalleryController {

    private static final String GALLERY_IMAGES_DIRECTORY = "C:\\Users\\Ryan\\Desktop\\OneilIndustries\\src\\main\\webapp\\WEB-INF\\view\\gallery\\images\\";
    private static final String FILE_NOT_EXISTS_ERROR_MESSAGE = ": Does not exist on this server";
    private static final String USER_NOT_AUTHORISED_MESSAGE = " is not authorised";

    private final ImageService imageService;
    private final AlbumService albumService;

    @Autowired
    public ImageGalleryController(ImageService imageService, AlbumService albumService) {
        this.imageService = imageService;
        this.albumService = albumService;
    }

    @GetMapping("gallery")
    public String showGallery(Model model) {
        model.addAttribute("images", this.imageService.getImagesByLinkStatus("public"));
        return "gallery/index";
    }

    @GetMapping(value = "gallery/images/{imageFileName}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageFileName") String imageFileName, Authentication user) throws IOException, NotAuthorisedException {
        Image image = imageService.getImageFileName(imageFileName);

        File serverFile = new File(GALLERY_IMAGES_DIRECTORY + imageFileName);

        if (!serverFile.exists() || !serverFile.isFile()) {
            throw new FileNotFoundException(imageFileName + FILE_NOT_EXISTS_ERROR_MESSAGE);
        }

        if (image.getLinkStatus().equalsIgnoreCase("private") && ( user == null || !user.getName().equalsIgnoreCase(image.getUploader())) ) {
            throw new NotAuthorisedException("You do not have the permission to view this image");
        }

        return Files.readAllBytes(serverFile.toPath());
    }

    @GetMapping(value = "gallery/images/thumbnail/{imageFileName}")
    @ResponseBody
    public byte[] getImageThumbnail(@PathVariable(value = "imageFileName") String imageFileName, Authentication user) throws IOException, NotAuthorisedException {
        Image image = imageService.getImageFileName(imageFileName);

        File serverFile = new File(GALLERY_IMAGES_DIRECTORY + "thumbnail/" + imageFileName);

        if (!serverFile.exists() || !serverFile.isFile()) {
            throw new FileNotFoundException(imageFileName + FILE_NOT_EXISTS_ERROR_MESSAGE);
        }

        if (image.getLinkStatus().equalsIgnoreCase("private") && (user == null || !user.getName().equalsIgnoreCase(image.getUploader())) ) {
            throw new NotAuthorisedException("You do not have the permission to view this image");
        }
        return Files.readAllBytes(serverFile.toPath());
    }

    @PostMapping(value = "gallery/upload", consumes = "multipart/form-data")
    public ModelAndView uploadImage(@RequestParam("image")MultipartFile file, @RequestParam String name, @RequestParam String privacy, @RequestParam String albumName,@RequestParam String newalbumName, @RequestParam(name = "showUnlistedImages") boolean showUnlistedImages, Authentication authentication) throws FileExistsException, IOException {
        if (!file.isEmpty()) {
            Image doesImageExistsAlready = imageService.getImageFileName(file.getOriginalFilename());

            if (doesImageExistsAlready != null) {
                throw new FileExistsException(file.getOriginalFilename() + " Already exists in database");
            }

            //Handles file writing
            FileHandler fileHandler = new FileHandler();
            fileHandler.writeFile(file,GALLERY_IMAGES_DIRECTORY);

            String fileName = file.getOriginalFilename();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();

            Image image = new Image(name, fileName ,privacy,authentication.getName(), localDate.format(dtf));

            Album newAlbum;
            if (!albumName.equalsIgnoreCase("none")) {
                Album doesAlbumExists = albumService.getAlbumByName(albumName);
                if (doesAlbumExists == null) {
                    newAlbum = new Album(newalbumName,authentication.getName(),showUnlistedImages);
                } else {
                    newAlbum = doesAlbumExists;
                    newAlbum.setCreator(authentication.getName());
                    albumService.saveAlbum(newAlbum);
                }
                image.setAlbumID(newAlbum.getId());
            }

            imageService.saveImage(image);
        }
        return new ModelAndView("redirect:/gallery/myimages/" + file.getOriginalFilename());
    }

    @GetMapping("gallery/upload")
    public String showUpload(Model model, Authentication user) {

        List<Album> albums = albumService.getAlbumsByCreator(user.getName());

        model.addAttribute("albums",albums);

        return "gallery/upload";
    }

    @GetMapping("gallery/myimages")
    public String showUserImages(Model model, Authentication user) {

        List<Image> userImages = imageService.getImagesByUser(user.getName());

        if (userImages != null) {
            model.addAttribute("userimages",userImages);
        }
        return "gallery/myimages";
    }

    @GetMapping("gallery/myimages/{imageFileName}")
    public String manageImage(@PathVariable(value = "imageFileName") String imageFileName, Model model, Authentication user) throws FileNotFoundException, NotAuthorisedException {
        Image image = imageService.getImageFileName(imageFileName);

        if (image == null) {
            throw new FileNotFoundException(imageFileName + FILE_NOT_EXISTS_ERROR_MESSAGE);
        }
        if (!image.getUploader().equals(user.getName())) {
            throw new NotAuthorisedException(user.getName() + USER_NOT_AUTHORISED_MESSAGE);
        }

        model.addAttribute("image", image);

        List<Album> albums = albumService.getAlbumsByCreator(user.getName());

        model.addAttribute("albums",albums);

        return "gallery/manageimage";
    }

    @PostMapping("gallery/update/{imageID}")
    public String updateImage(@PathVariable(value = "imageID") String id, @RequestParam(name = "imageName") String imageName, @RequestParam(name = "privacy") String linkStatus,@RequestParam(name = "albumName") String albumName,@RequestParam(name = "newalbumName") String newalbumName,@RequestParam(name = "showUnlistedImages") boolean showUnlistedImages, Authentication user) throws FileNotFoundException, NotAuthorisedException {
        Image image = imageService.getImage(Integer.valueOf(id));

        if (image == null) {
            throw new FileNotFoundException("Updating image wasn't found");
        }
        if (!image.getUploader().equals(user.getName())) {
            throw new NotAuthorisedException(user.getName() + USER_NOT_AUTHORISED_MESSAGE);
        }

        image.setName(imageName);
        image.setLinkStatus(linkStatus);

        if (!albumName.equalsIgnoreCase("none")) {
            Album album = albumService.getAlbumByName(albumName);

            if (album == null) {
                album = new Album(newalbumName,user.getName(),showUnlistedImages);
            }
            albumService.saveAlbum(album);
            image.setAlbumID(album.getId());
        }else {
            image.setAlbumID(null);
        }
        imageService.saveImage(image);

        return "redirect:/gallery/myimages/" + image.getFileName();
    }

    @GetMapping("/gallery/album/{albumName}")
    public String showAlbum(@PathVariable String albumName, Model model) throws FileNotFoundException {
        Album album = albumService.getAlbumByName(albumName);

        if (album == null) {
            throw new FileNotFoundException(albumName + ": Is not a valid album name");
        }


        List<Image> albumImages = imageService.getAlbumImages(album.getId());

        List<Image> removedImages = new ArrayList<>();
        for (Image image: albumImages) {
            if (!album.isShowUnlistedImages() && image.getLinkStatus().equalsIgnoreCase("unlisted") || image.getLinkStatus().equalsIgnoreCase("private")) {
                removedImages.add(image);
            }
        }
        albumImages.removeAll(removedImages);

        model.addAttribute("albumImages",albumImages);
        model.addAttribute("album", album);

        return "gallery/album";
    }

    @GetMapping("/gallery/myalbums")
    public String showMyAlbums(Authentication user, Model model) {

        List<Album> userAlbums = albumService.getAlbumsByCreator(user.getName());
        List<ImageAlbum> fullAlbums = new ArrayList<>();
        AlbumCreator albumCreator = new AlbumCreator(imageService);

        for (Album album: userAlbums) {
            ImageAlbum imageAlbum = albumCreator.createAlbum(album);
            if (imageAlbum != null) {
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

        ImageAlbum imageAlbum = new AlbumCreator(imageService).createAlbum(album);

        model.addAttribute("album",imageAlbum);

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

        List<Image> albumImages = imageService.getAlbumImages(album.getId());

        for (Image image: albumImages) {
            image.setAlbumID(null);
            imageService.saveImage(image);
        }
        albumService.deleteAlbum(album.getId());

        return "redirect:/gallery/myalbums";
    }
}