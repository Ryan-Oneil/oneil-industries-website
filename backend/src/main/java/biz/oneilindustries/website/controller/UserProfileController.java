package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.ApiToken;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.UpdatedUser;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    private final UserService userService;

    @Autowired
    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity showUserProfileHome(Authentication user) {

        HashMap<String, Object> userDetails = new HashMap<>();

        User userProfile = userService.getUser(user.getName());
        userProfile.setPassword(null);

        userDetails.put("user", new UpdatedUser(userProfile.getUsername(), userProfile.getEmail(), userProfile.getRole(), userProfile.getEnabled()));
        userDetails.put("storageQuota", userService.getQuotaByUsername(userProfile.getUsername()));
        userDetails.put("userDiscord", userService.getUserDiscordProfiles(user.getName()));
        userDetails.put("userTeamspeak", userService.getUserTeamspeakProfile(user.getName()));

        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/profile/storageQuota")
    public ResponseEntity getUserSpaceQuota(Authentication user) {
        return ResponseEntity.ok(userService.getQuotaByUsername(user.getName()));
    }

    @PostMapping("/profile/update")
    public ResponseEntity updateUserDetails(@RequestBody UpdatedUser updatedUser, Authentication authentication) {

        User user = userService.getUser(authentication.getName());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        User checkEmail = userService.getUserByEmail(updatedUser.getEmail());

        if ( checkEmail != null && !updatedUser.getEmail().equalsIgnoreCase(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        userService.updateUser(updatedUser, authentication.getName());

        return ResponseEntity.ok("Successfully updated account details");
    }

    @GetMapping("/profile/getAPIToken")
    public ResponseEntity getAPIJWT(Authentication authentication) {
        return ResponseEntity.ok(userService.getApiTokensByUsername(authentication.getName()).getToken());
    }

    @GetMapping("/profile/generateAPIToken")
    public ResponseEntity generateAPIJWT(Authentication authentication) {

        ApiToken apiToken = userService.getApiTokensByUsername(authentication.getName());

        //Deletes existing token
        if (apiToken != null) {
            userService.deleteApiToken(apiToken);
        }
        apiToken = userService.generateApiToken(authentication.getName());

        return ResponseEntity.ok(apiToken.getToken());
    }

    @GetMapping("/profile/getShareX")
    public ResponseEntity generateShareXFile(Authentication authentication) {
        return ResponseEntity.ok(userService.generateShareXAPIFile(authentication.getName()));
    }
}
