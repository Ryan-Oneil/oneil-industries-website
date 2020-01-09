package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.service.RoleService;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.UpdatedQuota;
import biz.oneilindustries.website.validation.UpdatedUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    public AdminController(UserService userService, MediaService mediaService, RoleService roleService) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.roleService = roleService;
    }

    // User related admin apis

    @GetMapping("/users")
    public List<UpdatedUser> showUsers() {

        List<UpdatedUser> users = new ArrayList<>();

        for (User user : userService.getUsers()) {
            users.add(new UpdatedUser(user.getUsername(), "*", user.getEmail(), user.getRole(), user.getEnabled()));
        }
        return users;
    }

    @GetMapping("/users/roles")
    public ResponseEntity getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity getUserAccountInformation(@PathVariable String username) {
        User user = userService.getUser(username);

        if (user == null) {
            throw new UsernameNotFoundException(username + " doesn't exists");
        }
        //Create a new hashmap containing all this information and return the map for JSON
        HashMap<String, Object> userInformation = new HashMap<>();
        userInformation.put("details", new UpdatedUser(user.getUsername(), "*", user.getEmail(), user.getRole(), user.getEnabled()));
        userInformation.put("storageQuota", userService.getQuotaByUsername(username));
        userInformation.put("discordProfiles", userService.getUserDiscordProfiles(username));
        userInformation.put("teamspeakProfiles", userService.getUserTeamspeakProfile(username));

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
    public ResponseEntity getAllMedias() {
        return ResponseEntity.ok(mediaService.getMedias());
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
}
