package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.dto.QuotaDTO;
import biz.oneilindustries.website.dto.ShareXConfig;
import biz.oneilindustries.website.dto.UserDTO;
import biz.oneilindustries.website.entity.ApiToken;
import biz.oneilindustries.website.entity.Role;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.UpdatedQuota;
import biz.oneilindustries.website.validation.UpdatedUser;
import java.util.List;
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

    @GetMapping("/{username}/getAPIToken")
    public ResponseEntity<String> getAPIJWT(@PathVariable String username, Authentication authentication) {
        return ResponseEntity.ok(userService.getApiTokenByUser(username).getToken());
    }

    @GetMapping("/{username}/generateAPIToken")
    public ResponseEntity<String> generateAPIJWT(@PathVariable String username, Authentication authentication) {
        ApiToken apiToken = userService.getApiTokenByUser(username);

        //Deletes existing token
        if (apiToken != null) {
            userService.deleteApiToken(apiToken);
        }
        ApiToken token = userService.generateApiToken(username);

        return ResponseEntity.ok(token.getToken());
    }

    @GetMapping("/{username}/getShareX")
    public ResponseEntity<ShareXConfig> getShareXConfig(@PathVariable String username, Authentication authentication) {
        return ResponseEntity.ok(userService.generateShareXAPIFile(username));
    }

    //admin related apis
    @GetMapping("/admin/users")
    public List<UserDTO> showUsers() {
        return userService.getUsers();
    }

    @GetMapping("/admin/roles")
    public ResponseEntity<List<Role>> adminGetRoles() {
        return ResponseEntity.ok(userService.getRoles());
    }

    @PutMapping("/admin/user/{username}/update/quota")
    public ResponseEntity<HttpStatus> adminUpdateUserQuota(@PathVariable String username, @RequestBody UpdatedQuota updatedQuota) {
        userService.updateUserQuota(updatedQuota, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/admin/user/{username}/disable")
    public ResponseEntity<HttpStatus> adminDisableUser(@PathVariable String username) {
        userService.changeUserAccountStatus(false, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/admin/user/{username}/enable")
    public ResponseEntity<HttpStatus> adminEnableUser(@PathVariable String username) {
        userService.changeUserAccountStatus(true, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
