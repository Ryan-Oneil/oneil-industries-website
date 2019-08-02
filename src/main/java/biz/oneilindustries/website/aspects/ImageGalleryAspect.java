package biz.oneilindustries.website.aspects;

import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.exception.NotAuthorisedException;
import biz.oneilindustries.website.service.AlbumService;
import biz.oneilindustries.website.service.MediaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;

@Component
@Aspect
public class ImageGalleryAspect {

    private final MediaService mediaService;
    private final AlbumService albumService;

    private static final String FILE_NOT_EXISTS_ERROR_MESSAGE = ": Does not exist on this server";
    private static final String GALLERY_IMAGES_DIRECTORY = "C:\\Users\\Ryan\\Desktop\\OneilIndustries\\src\\main\\webapp\\WEB-INF\\view\\gallery\\images\\";
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

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.manageMedia(..))")
    private void manageMedia() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.updateMedia(..))")
    private void updateMedia() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.deleteMedia(..))")
    private void deleteMedia() {}

    @Pointcut("manageMedia() || deleteMedia() || updateMedia()")
    private void combinedMediaManagement() {}

    // Album aspects
    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.manageMyAlbums(..))")
    private void manageAlbum() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.changeAlbum(..))")
    private void changeAlbum() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.deleteAlbum(..))")
    private void deleteAlbum() {}

    @Pointcut("manageAlbum() || changeAlbum() || deleteAlbum()")
    private void combinedAlbums() {}


    @Before("displayMedia()")
    public void before(JoinPoint joinPoint) throws FileNotFoundException {

        Object[] args = joinPoint.getArgs();

        String mediaFileName = (String) args[0];
        Authentication user = (Authentication) args[1];

        Media media = mediaService.getMediaFileName(mediaFileName);

        File serverFile = new File(GALLERY_IMAGES_DIRECTORY + mediaFileName);

        if (!serverFile.exists() || !serverFile.isFile()) {
            throw new FileNotFoundException(mediaFileName + FILE_NOT_EXISTS_ERROR_MESSAGE);
        }

        if (media.getLinkStatus().equalsIgnoreCase("private") && ( user == null || !user.getName().equalsIgnoreCase(media.getUploader())) ) {
            throw new NotAuthorisedException(NO_PERMISSION);
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

        String mediaFileName = (String) args[0];
        Authentication user = (Authentication) args[1];
        HttpServletRequest request = (HttpServletRequest) args[2];

        Media media = mediaService.getMediaFileName(mediaFileName);

        if (media == null) {
            throw new FileNotFoundException(mediaFileName + FILE_NOT_EXISTS_ERROR_MESSAGE);
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
