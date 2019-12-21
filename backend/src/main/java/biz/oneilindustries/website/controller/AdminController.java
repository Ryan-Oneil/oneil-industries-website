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
@RequestMapping("/api/admin")
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

    @GetMapping("/users")
    public List<UpdatedUser> showUsers() {

        List<UpdatedUser> users = new ArrayList<>();

        for (User user : userService.getUsers()) {
            users.add(new UpdatedUser(user.getUsername(), "*", user.getEmail(), user.getRole(), user.getEnabled()));
        }
        return users;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity manageUser(@PathVariable String username) {
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

    @PutMapping("/updateUser/{username}")
    public ResponseEntity updateUser(@RequestBody @Valid UpdatedUser updatedUser, @PathVariable String username) {
        userService.updateUser(updatedUser,username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/updateUserQuota/{username}")
    public ResponseEntity updateUserQuota(@RequestBody @Valid UpdatedQuota updatedQuota, @PathVariable String username) {
        userService.updateUserQuota(updatedQuota, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }
    @GetMapping("/user/{username}/hideMedia")
    public ResponseEntity hideUserMedia(@PathVariable String username) {
        mediaService.hideAllMedia(username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }
}
