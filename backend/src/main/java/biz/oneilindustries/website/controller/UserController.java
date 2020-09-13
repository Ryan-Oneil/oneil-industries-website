package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.dto.QuotaDTO;
import biz.oneilindustries.website.dto.UserDTO;
import biz.oneilindustries.website.entity.ApiToken;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.UpdatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}/details")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable String username, Authentication user) {
        return ResponseEntity.ok(userService.getUserStats(username));
    }

    @GetMapping("/{username}/quota")
    public ResponseEntity<QuotaDTO> getUserQuota(@PathVariable String username, Authentication user) {
        return ResponseEntity.ok(userService.quotaToDTO(userService.getQuotaByUsername(username)));
    }

    @PutMapping("/{username}/details/update")
    public ResponseEntity<HttpStatus> updateUserDetails(@PathVariable String username, Authentication user, @RequestBody UpdatedUser updatedUser) {
        userService.updateUser(updatedUser, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/getAPIToken")
    public ResponseEntity getAPIJWT(Authentication authentication) {
        return ResponseEntity.ok(userService.getApiTokenByUser(authentication.getName()).getToken());
    }

    @GetMapping("/generateAPIToken")
    public ResponseEntity generateAPIJWT(Authentication authentication) {
        ApiToken apiToken = userService.getApiTokenByUser(authentication.getName());

        //Deletes existing token
        if (apiToken != null) {
            userService.deleteApiToken(apiToken);
        }
        ApiToken token = userService.generateApiToken(authentication.getName());

        return ResponseEntity.ok(token.getToken());
    }

    @GetMapping("/getShareX")
    public ResponseEntity generateShareXFile(Authentication authentication) {
        return ResponseEntity.ok(userService.generateShareXAPIFile(authentication.getName()));
    }
}
