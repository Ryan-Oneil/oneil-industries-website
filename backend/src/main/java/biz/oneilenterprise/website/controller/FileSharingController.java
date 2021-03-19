package biz.oneilenterprise.website.controller;


import biz.oneilenterprise.website.dto.FileDTO;
import biz.oneilenterprise.website.dto.LinkDTO;
import biz.oneilenterprise.website.entity.Link;
import biz.oneilenterprise.website.entity.SharedFile;
import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.exception.LinkException;
import biz.oneilenterprise.website.service.ShareLinkService;
import biz.oneilenterprise.website.service.SystemFileService;
import biz.oneilenterprise.website.service.UserService;
import biz.oneilenterprise.website.validation.ShareLinkForm;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class FileSharingController {

    private final SystemFileService systemFileService;
    private final UserService userService;
    private final ShareLinkService linkService;

    @Value("${service.frontendUrl}")
    private String frontendUrl;

    public FileSharingController(SystemFileService systemFileService, UserService userService,
        ShareLinkService linkService) {
        this.systemFileService = systemFileService;
        this.userService = userService;
        this.linkService = linkService;
    }

    @PostMapping("/share")
    public ResponseEntity<String> createShareLink(@Valid ShareLinkForm form, BindingResult result, HttpServletRequest request, Authentication username)
        throws ParseException, IOException, FileUploadException {

        if (result.hasErrors()) {
            throw new LinkException(result.getFieldErrors().get(0).getDefaultMessage());
        }
        User user = (User) username.getPrincipal();
        long remainingQuota = userService.getRemainingQuota(user.getUsername());

        List<File> uploadedFiles = systemFileService.handleFileUpload(request, remainingQuota,
            linkService.getLinkDirectory(user.getUsername(), linkService.generateLinkUUID(7)), false);

        Link link = linkService.generateShareLink(user, form.getExpires(), form.getTitle(), uploadedFiles);
        userService.increaseUsedAmount( user.getUsername(), link.getSize());

        return ResponseEntity.ok(frontendUrl + "/shared/" + link.getId());
    }

    @GetMapping("/info/{link}")
    public ResponseEntity<LinkDTO> viewLink(@PathVariable String link, HttpServletRequest request) {
        Link sharedLink = linkService.getLinkFileWithValidation(link);

        List<FileDTO> files = linkService.filesToDTO(sharedLink.getFiles());
        LinkDTO linkDTO = linkService.linkToDTO(sharedLink, files);

        linkService.registerLinkView(sharedLink, request.getRemoteAddr());

        return ResponseEntity.ok(linkDTO);
    }

    @GetMapping("/download/{link}")
    public void downloadFiles(@PathVariable String link, HttpServletResponse response) throws IOException {
        Link sharedLink = linkService.getLinkValidate(link);

        String fileName = sharedLink.getTitle().isEmpty() ? sharedLink.getId() : sharedLink.getTitle();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s.zip", fileName));

        systemFileService.streamFolderAsZip(linkService.getLinkDirectory(sharedLink.getCreator().getUsername(), sharedLink.getId()),
            response.getOutputStream());
    }

    @DeleteMapping("/delete/{linkID}")
    public ResponseEntity<HttpStatus> deleteSharedLink(@PathVariable String linkID, Authentication user) {
        Link link = linkService.deleteLink(linkID);
        userService.decreaseUsedAmount(user.getName(), link.getSize());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/link/add/{linkID}")
    public ResponseEntity<List<FileDTO>> addFilesToLink(@PathVariable String linkID, Authentication username, HttpServletRequest request)
        throws IOException, FileUploadException {

        User user = linkService.getLinkFileWithValidation(linkID).getCreator();
        long remainingQuota = userService.getRemainingQuota(user.getUsername());

        List<File> uploadedFiles = systemFileService.handleFileUpload(request, remainingQuota,
                linkService.getLinkDirectory(user.getUsername(), linkService.generateLinkUUID(7)), false);

        List<FileDTO> files = linkService.addFilesToLink(linkID, uploadedFiles);

        long sizeOfFiles = files.stream().mapToLong(FileDTO::getSize).sum();
        userService.increaseUsedAmount(user.getUsername(), sizeOfFiles);

        return ResponseEntity.ok(files);
    }

    @PutMapping("/link/edit/{linkID}")
    public ResponseEntity<HttpStatus> editLink(@PathVariable String linkID, Authentication username, @Valid @RequestBody ShareLinkForm form,
        BindingResult result) throws ParseException {

        if (result.hasErrors()) {
            throw new LinkException(result.getFieldErrors().get(0).getDefaultMessage());
        }
        linkService.editLink(linkID, form.getTitle(), form.getExpires());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/file/dl/{fileID}")
    public ResponseEntity<StreamingResponseBody> downloadFileFromLink(@PathVariable String fileID, HttpServletResponse response) {
        SharedFile file = linkService.checkFileLinkValidation(fileID);
        Link sharedLink = file.getLink();

        StreamingResponseBody streamedFile = systemFileService.streamFile(linkService.getFileLocation(sharedLink.getCreator().getUsername(),
            sharedLink.getId(), file.getName()));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s", file.getName()));

        return ResponseEntity.status(HttpStatus.OK).contentLength(file.getSize()).cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)).body(streamedFile);
    }

    @DeleteMapping("/file/delete/{fileID}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable String fileID, Authentication user) {
        SharedFile deletedFile = linkService.deleteFile(fileID);
        userService.decreaseUsedAmount(user.getName(), deletedFile.getSize());

        return ResponseEntity.ok(HttpStatus.OK);
    }

//    User related APIs

    @GetMapping("/user/{username}/links")
    public ResponseEntity<HashMap<String, Object>> displayUsersLink(@PathVariable String username, Authentication user, Pageable pageable) {
        HashMap<String, Object> links = linkService.getUserLinks(username, pageable);

        return ResponseEntity.ok(links);
    }

    @GetMapping("/user/{username}/links/stats")
    public ResponseEntity<HashMap<String, Object>> displayUserLinkStats(@PathVariable String username, Authentication user) {
        HashMap<String, Object> userStats = linkService.getUserStats(username);

        return ResponseEntity.ok(userStats);
    }

    @GetMapping("/user/{username}/links/stat/total")
    public ResponseEntity<Integer> displayUserTotalLinkCount(@PathVariable String username, Authentication user) {
        int totalLinks = linkService.getUserTotalLinkCount(username);

        return ResponseEntity.ok(totalLinks);
    }

    //Admin related apis

    @GetMapping("/admin/link/stats")
    public ResponseEntity<HashMap<String, Object>> displayAdminLinkStats() {
        HashMap<String, Object> adminStats = linkService.getAdminLinkStats();

        return ResponseEntity.ok(adminStats);
    }

    @GetMapping("/admin/links")
    public ResponseEntity<HashMap<String, Object>> displayAllLinksFromRange(Pageable pageable) {
        HashMap<String, Object> links = linkService.getLinksPageable(pageable);

        return ResponseEntity.ok(links);
    }
}