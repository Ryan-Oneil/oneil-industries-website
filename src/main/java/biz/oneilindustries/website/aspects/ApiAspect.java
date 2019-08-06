package biz.oneilindustries.website.aspects;

import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.service.MediaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Aspect
public class ApiAspect {

    @Autowired
    private MediaService mediaService;

    @Pointcut("execution(* biz.oneilindustries.website.controller.GalleryRestController.uploadMediaAPI(..))")
    private void uploadAPI() {}

    @Before("uploadAPI()")
    public void performValidation(JoinPoint joinpoint) {

        MultipartFile file = (MultipartFile) joinpoint.getArgs()[0];

        if (file.getSize() == 0) {
            throw new MediaException("File not found");
        }

        Media doesMediaExistsAlready = mediaService.getMediaFileName(file.getOriginalFilename());

        if (doesMediaExistsAlready != null) {
            throw new MediaException(file.getOriginalFilename() + " Already exists in database");
        }
    }
}
