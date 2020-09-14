package biz.oneilindustries.website.controller;

import static biz.oneilindustries.AppConfig.GALLERY_IMAGES_DIRECTORY;

import biz.oneilindustries.website.service.AlbumService;
import biz.oneilindustries.website.service.ContactService;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.io.FileSystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final MediaService mediaService;
    private final AlbumService albumService;
    private final ContactService contactService;

    @Autowired
    public AdminController(UserService userService, MediaService mediaService,
        AlbumService albumService, ContactService contactService) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.albumService = albumService;
        this.contactService = contactService;
    }

    @GetMapping("/stats")
    public ResponseEntity getDashboardStats() throws IOException {
        HashMap<String, Object> stats = new HashMap<>();

        stats.put("totalMedia", mediaService.getTotalMedias());
        stats.put("totalAlbums", albumService.getAlbums().size());
        stats.put("totalUsers", userService.getUsers().size());
        stats.put("recentUsers", userService.getRecentUsers());
        stats.put("remainingStorage", FileSystemUtils.freeSpaceKb(GALLERY_IMAGES_DIRECTORY));
        stats.put("usedStorage", userService.getTotalUsedQuota());
        stats.put("feedback", contactService.getRecentFeedbacks(3));

        return ResponseEntity.ok(stats);
    }
}
