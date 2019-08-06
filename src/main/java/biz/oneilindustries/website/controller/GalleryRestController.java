package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.filecreater.FileHandler;
import biz.oneilindustries.website.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class GalleryRestController {

    private final String galleryImagesDirectory;

    private final MediaService mediaService;

    @Autowired
    public GalleryRestController(ServletContext servletContext, MediaService mediaService) {
        galleryImagesDirectory = servletContext.getRealPath("/WEB-INF/view/gallery/images/");
        this.mediaService = mediaService;
    }

    @PostMapping("/api/gallery/upload")
    public String uploadMediaAPI(@RequestBody MultipartFile file, @RequestBody(required = false) String privacy, Authentication user, HttpServletRequest request) throws IOException {

        mediaService.registerMedia(file, user.getName(), privacy);

        FileHandler.writeFile(file, galleryImagesDirectory);

        return request.getLocalName() + "/gallery/images/" + file.getOriginalFilename();
    }

}
