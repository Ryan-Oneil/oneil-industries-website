package biz.oneilindustries.website.controller;

import static biz.oneilindustries.AppConfig.GALLERY_IMAGES_DIRECTORY;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.service.AlbumService;
import biz.oneilindustries.website.service.ContactService;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.service.RoleService;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.UpdatedQuota;
import biz.oneilindustries.website.validation.UpdatedUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.io.FileSystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final MediaService mediaService;
    private final RoleService roleService;
    private final AlbumService albumService;
    private final ContactService contactService;

    @Autowired
    public AdminController(UserService userService, MediaService mediaService, RoleService roleService,
        AlbumService albumService, ContactService contactService) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.roleService = roleService;
        this.albumService = albumService;
        this.contactService = contactService;
    }

    // User related admin apis

    @GetMapping("/users")
    public List<UpdatedUser> showUsers() {
        List<UpdatedUser> users = new ArrayList<>();

        userService.getUsers().forEach(user -> users.add(new UpdatedUser(user.getUsername(), "*", user.getEmail(), user.getRole(), user.getEnabled())));

        return users;
    }

    @GetMapping("/users/roles")
    public ResponseEntity getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity getUserAccountInformation(@PathVariable String username) {
        User user = userService.getUser(username);

        //Create a new hashmap containing all this information and return the map for JSON
        HashMap<String, Object> userInformation = new HashMap<>();
        userInformation.put("details", new UpdatedUser(user.getUsername(), "*", user.getEmail(), user.getRole(), user.getEnabled()));
        userInformation.put("storageQuota", userService.getQuotaByUsername(username));

        return ResponseEntity.ok(userInformation);
    }

    @PutMapping("/user/{username}/update/details")
    public ResponseEntity updateUser(@RequestBody @Valid UpdatedUser updatedUser, @PathVariable String username) {
        userService.updateUser(updatedUser,username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/user/{username}/update/quota")
    public ResponseEntity updateUserQuota(@RequestBody @Valid UpdatedQuota updatedQuota, @PathVariable String username) {
        userService.updateUserQuota(updatedQuota, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/user/{username}/medias/hide")
    public ResponseEntity hideUserMedia(@PathVariable String username) {
        mediaService.hideAllMedia(username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Media related admin apis

    @GetMapping("/medias")
    public ResponseEntity getAllMedias(Pageable pageable) {
        return ResponseEntity.ok(mediaService.getMedias(pageable));
    }

    @GetMapping("/medias/pendingApproval")
    public ResponseEntity getMediasRequiringApproval() {
        return ResponseEntity.ok(mediaService.getMediaApprovalsByStatus("pending"));
    }

    @PutMapping("/media/{approvalMediaID}/approve")
    public ResponseEntity approveMediaApproval(@PathVariable int approvalMediaID) {
        mediaService.approvePublicMedia(approvalMediaID);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/media/{approvalMediaID}/deny")
    public ResponseEntity denyMediaApproval(@PathVariable int approvalMediaID) {
        mediaService.setMediaApprovalStatus(approvalMediaID, "denied");

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity getDashboardStats() throws IOException {
        HashMap<String, Object> stats = new HashMap<>();

        List<User> users =  userService.getRecentUsers();

        for (User user: users) {
            user.setPassword("*");
        }
        stats.put("totalMedia", mediaService.getTotalMedias());
        stats.put("totalAlbums", albumService.getAlbums().size());
        stats.put("totalUsers", userService.getUsers().size());
        stats.put("recentUsers", users);
        stats.put("remainingStorage", FileSystemUtils.freeSpaceKb(GALLERY_IMAGES_DIRECTORY));
        stats.put("feedback", contactService.getRecentFeedbacks(3));

        return ResponseEntity.ok(stats);
    }
}
