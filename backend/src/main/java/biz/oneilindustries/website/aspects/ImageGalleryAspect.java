package biz.oneilindustries.website.aspects;

import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.exception.NotAuthorisedException;
import biz.oneilindustries.website.service.MediaService;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ImageGalleryAspect {

    private final MediaService mediaService;

    private static final String NO_PERMISSION = "You don't have the permission to access this resource";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Autowired
    public ImageGalleryAspect(MediaService mediaService) {
        this.mediaService = mediaService;
    }

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

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.updateAlbum(..))")
    private void updateAlbum() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.ImageGalleryController.deleteAlbum(..))")
    private void deleteAlbum() {}

    @Pointcut("manageAlbum() || updateAlbum() || deleteAlbum()")
    private void combinedAlbums() {}

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
    public void checkMedia(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        int mediaInt = (int) args[0];
        Authentication user = (Authentication) args[1];
        HttpServletRequest request = (HttpServletRequest) args[2];

        Media media = mediaService.getMedia(mediaInt);

        if (!media.getUploader().equals(user.getName()) && !request.isUserInRole(ADMIN_ROLE)) {
            throw new NotAuthorisedException(user.getName() + NO_PERMISSION);
        }
    }

    @Before("combinedAlbums()")
    public void checkAlbum(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        String albumName = (String) args[0];
        Authentication user = (Authentication) args[1];
        HttpServletRequest request = (HttpServletRequest) args[2];

        Album album = mediaService.getAlbum(albumName);

        if (!user.getName().equalsIgnoreCase(album.getCreator()) && !request.isUserInRole(ADMIN_ROLE)) {
            throw new NotAuthorisedException(NO_PERMISSION);
        }
    }
}
