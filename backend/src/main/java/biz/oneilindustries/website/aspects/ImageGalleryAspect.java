package biz.oneilindustries.website.aspects;

import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.exception.NotAuthorisedException;
import biz.oneilindustries.website.service.AlbumService;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.validation.GalleryUpload;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Aspect
public class ImageGalleryAspect {

    private final MediaService mediaService;
    private final AlbumService albumService;

    private static final String FILE_NOT_EXISTS_ERROR_MESSAGE = ": Does not exist on this server";
    private static final String GALLERY_IMAGES_DIRECTORY = "E:/images/";
    private static final String NO_PERMISSION = "You don't have the permission to access this resource";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Autowired
    public ImageGalleryAspect(MediaService mediaService, AlbumService albumService) {
        this.mediaService = mediaService;
        this.albumService = albumService;
    }

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.getMedia*(..))")
    private void displayMedia() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.showUser*(..))")
    private void showUserMedia() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.updateMedia(..))")
    private void updateMedia() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.deleteMedia(..))")
    private void deleteMedia() {}

    @Pointcut("deleteMedia() || updateMedia()")
    private void combinedMediaManagement() {}

    // Album aspects
    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.manageMyAlbums(..))")
    private void manageAlbum() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.showUserAlbumNames(..))")
    private void showAlbumNames() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.updateAlbum(..))")
    private void updateAlbum() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.deleteAlbum(..))")
    private void deleteAlbum() {}

    @Pointcut("manageAlbum() || updateAlbum() || deleteAlbum()")
    private void combinedAlbums() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.uploadMediaAPI(..))")
    private void uploadAPI() {}

    @Before("uploadAPI()")
    public void performValidation(JoinPoint joinpoint) {

        GalleryUpload galleryUpload = (GalleryUpload) joinpoint.getArgs()[0];

        MultipartFile file = galleryUpload.getFile();

        if (file.getSize() == 0) {
            throw new MediaException("File not found");
        }

        Media doesMediaExistsAlready = mediaService.getMediaFileName(file.getOriginalFilename());

        if (doesMediaExistsAlready != null) {
            throw new MediaException(file.getOriginalFilename() + " Already exists in database");
        }
    }

    @Before("displayMedia()")
    public void before(JoinPoint joinPoint) throws FileNotFoundException {

        Object[] args = joinPoint.getArgs();

        int mediaID = (int) args[0];

        Media media = mediaService.getMedia(mediaID);

        if (media == null) {
            throw new MediaException("Media doesn't exist");
        }
    }

    @Before("showUserMedia()")
    public void checkPermission(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        Authentication user = (Authentication) args[0];
        String username = (String) args[1];
        HttpServletRequest request = (HttpServletRequest) args[2];

        if (!user.getName().equalsIgnoreCase(username) && !request.isUserInRole(ADMIN_ROLE)) {
            throw new NotAuthorisedException(NO_PERMISSION);
        }
    }

    @Before("combinedMediaManagement()")
    public void checkMedia(JoinPoint joinPoint) throws FileNotFoundException {

        Object[] args = joinPoint.getArgs();

        int mediaInt = (int) args[0];
        Authentication user = (Authentication) args[1];
        HttpServletRequest request = (HttpServletRequest) args[2];

        Media media = mediaService.getMedia(mediaInt);

        if (media == null) {
            throw new FileNotFoundException(FILE_NOT_EXISTS_ERROR_MESSAGE);
        }
        if (!media.getUploader().equals(user.getName()) && !request.isUserInRole(ADMIN_ROLE)) {
            throw new NotAuthorisedException(user.getName() + NO_PERMISSION);
        }
    }

    @Before("combinedAlbums()")
    public void checkAlbum(JoinPoint joinPoint) throws FileNotFoundException {

        Object[] args = joinPoint.getArgs();

        String albumName = (String) args[0];
        Authentication user = (Authentication) args[1];
        HttpServletRequest request = (HttpServletRequest) args[2];

        Album album = albumService.getAlbumByName(albumName);

        if (album == null) {
            throw new FileNotFoundException(albumName + FILE_NOT_EXISTS_ERROR_MESSAGE);
        }

        if (!user.getName().equalsIgnoreCase(album.getCreator()) && !request.isUserInRole(ADMIN_ROLE)) {
            throw new NotAuthorisedException(NO_PERMISSION);
        }
    }
}
