package biz.oneilenterprise.website.controller;

import biz.oneilenterprise.website.service.MediaService;
import biz.oneilenterprise.website.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.io.FileSystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class  AdminController {

    private final UserService userService;
    private final MediaService mediaService;

    @Value("${service.media.location}")
    private String mediaDirectory;

    public AdminController(UserService userService, MediaService mediaService) {
        this.userService = userService;
        this.mediaService = mediaService;
    }

    @GetMapping("/stats")
    public ResponseEntity<HashMap<String, Object>> getDashboardStats() throws IOException {
        HashMap<String, Object> stats = new HashMap<>();

        stats.put("totalMedia", mediaService.getTotalMedias());
        stats.put("totalAlbums", mediaService.getTotalAlbums());
        stats.put("totalUsers", userService.getUsers().size());
        stats.put("recentUsers", userService.getRecentUsers());
        stats.put("remainingStorage", FileSystemUtils.freeSpaceKb(mediaDirectory));
        stats.put("usedStorage", userService.getTotalUsedQuota());
        stats.put("recentMedia", mediaService.getMedias(PageRequest.of(0, 5)).get("medias"));

        return ResponseEntity.ok(stats);
    }
}
