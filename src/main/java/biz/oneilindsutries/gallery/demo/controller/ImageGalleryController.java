package biz.oneilindsutries.gallery.demo.controller;

import biz.oneilindsutries.gallery.demo.entity.Image;
import biz.oneilindsutries.gallery.demo.filecreater.FileHandler;
import biz.oneilindsutries.gallery.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class ImageGalleryController {

    private static final String GALLERY_IMAGES_DIRECTORY = "C:\\Users\\Ryan\\Desktop\\OneilIndustries\\src\\main\\webapp\\WEB-INF\\view\\gallery\\images\\";

    private final ImageService imageService;

    @Autowired
    public ImageGalleryController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("gallery")
    public String showGallery(Model model) {
        model.addAttribute("images", this.imageService.getPublicImages());
        return "gallery/index";
    }

    @GetMapping(value = "gallery/images/{imageFileName}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageFileName") String imageFileName) throws IOException {
        File serverFile = new File(GALLERY_IMAGES_DIRECTORY + imageFileName);

        return Files.readAllBytes(serverFile.toPath());
    }

    @GetMapping(value = "gallery/images/thumbnail/{imageFileName}")
    @ResponseBody
    public byte[] getImageThumbnail(@PathVariable(value = "imageFileName") String imageFileName) throws IOException {
        File serverFile = new File(GALLERY_IMAGES_DIRECTORY + "thumbnail/" + imageFileName);

        return Files.readAllBytes(serverFile.toPath());
    }

    @PostMapping(value = "gallery/upload", consumes = "multipart/form-data")
    public String uploadImage(@RequestParam("image")MultipartFile file, @RequestParam String name, @RequestParam String privacy, Authentication authentication) throws IOException {
        if (!file.isEmpty()) {
            Image doesImageExistsAlready = imageService.getImageFileName(file.getOriginalFilename());

            if (doesImageExistsAlready != null) {
                return "gallery/upload?fileFound";
            }

            //Handles file writing
            FileHandler fileHandler = new FileHandler();
            fileHandler.writeFile(file,GALLERY_IMAGES_DIRECTORY);

            String fileName = file.getOriginalFilename();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();

            Image image = new Image(name, fileName ,privacy,authentication.getName(), localDate.format(dtf));

            imageService.saveImage(image);
        }
        return "gallery/upload";
    }

    @GetMapping("gallery/upload")
    public String showUpload() {
        return "gallery/upload";
    }
}
