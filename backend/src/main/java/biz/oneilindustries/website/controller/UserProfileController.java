package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.pojo.ServiceClient;
import biz.oneilindustries.website.service.ManagerService;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.UpdatedUser;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final UserService userService;
    private final ManagerService managerService;

    @Autowired
    public UserProfileController(UserService userService, ManagerService managerService) {
        this.userService = userService;
        this.managerService = managerService;
    }

    @GetMapping("/profile")
    public ResponseEntity showUserProfileHome(Authentication user) {

        HashMap<String, Object> userDetails = new HashMap<>();

        User userProfile = userService.getUser(user.getName());
        userProfile.setPassword(null);

        userDetails.put("user", userProfile);
        userDetails.put("userDiscord", userService.getUserDiscordProfiles(user.getName()));
        userDetails.put("userTeamspeak", userService.getUserTeamspeakProfile(user.getName()));

        return ResponseEntity.ok(userDetails);
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

    @PostMapping("/profile/addservice/{service}")
    public ResponseEntity addUserService(@PathVariable String service, @RequestBody ServiceClient serviceClient, Authentication user, HttpServletRequest request) {

        if (request.isUserInRole("ROLE_UNREGISTERED")) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your account must be approved to register a service");
        }

        // Returns the hibernate entity as response for frontend consumption
        if (service.equalsIgnoreCase("teamspeak")) {
            return ResponseEntity.ok(managerService.addTeamspeakService(user.getName(), serviceClient));
        }else if (service.equalsIgnoreCase("discord")) {
            return ResponseEntity.ok(managerService.addDiscordService(user.getName(), serviceClient));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
    }

    @DeleteMapping("/profile/service/teamspeak/delete/{id}")
    public ResponseEntity deleteUserTeamspeakService(@PathVariable int id, Authentication user, HttpServletRequest request) {

        TeamspeakUser teamspeakUser = userService.getTeamspeakByID(id);

        if (teamspeakUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested account was not found");
        }
        if (!teamspeakUser.getUsername().equalsIgnoreCase(user.getName()) && !request.isUserInRole(ADMIN_ROLE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete service profile accounts that you don't own");
        }
        userService.deleteTeamspeakUUID(teamspeakUser);

        return ResponseEntity.ok("Teamspeak account has been unregistered");
    }

    @DeleteMapping("/profile/service/discord/delete/{id}")
    public ResponseEntity deleteUserDiscordService(@PathVariable int id, Authentication user, HttpServletRequest request) {
        DiscordUser discordUser = userService.getDiscordById(id);

        if (discordUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested account was not found");
        }
        if (!discordUser.getUsername().equalsIgnoreCase(user.getName()) && !request.isUserInRole(ADMIN_ROLE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete service profile accounts that you don't own");
        }
        userService.deleteDiscordUUID(discordUser);

        return ResponseEntity.ok("Discord account has been unregistered");
    }
}
