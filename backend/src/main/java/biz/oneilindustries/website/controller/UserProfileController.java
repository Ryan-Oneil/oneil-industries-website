package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.exception.NotAuthorisedException;
import biz.oneilindustries.website.exception.ServiceProfileException;
import biz.oneilindustries.website.pojo.ServiceClient;
import biz.oneilindustries.website.service.ManagerService;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.UpdatedUser;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        List<ServiceClient> tsClients = new ArrayList<>();
        List<String> registeredClients = userService.getTeamspeakUUIDs();

        for (Client client : managerService.getTSClients()) {
            if (!registeredClients.contains(client.getUniqueIdentifier())) {
                tsClients.add(new ServiceClient(client.getNickname(), client.getUniqueIdentifier()));
            }
        }
        registeredClients = userService.getDiscordUUIDs();
        List<ServiceClient> discordClients = new ArrayList<>();

        for (Member member : managerService.getDiscordMembers()) {
            if (!registeredClients.contains(member.getId())) {
                discordClients.add(new ServiceClient(member.getEffectiveName(), member.getId()));
            }
        }
        userDetails.put("teamspeakUsers", tsClients);
        userDetails.put("discordUsers", discordClients);

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

    @PostMapping("/profile/servicesAdd")
    public String addUserService(@RequestParam(value = "teamspeak", required = false) String teamspeakUUID, @RequestParam(value = "discord", required = false) String discordUUID, Authentication user, HttpServletRequest request, Model model) {

        if (request.isUserInRole("ROLE_UNREGISTERED")) {
            model.addAttribute("msg", "Your account must be approved to register a service");
            return "/profile/confirmservice";
        }

        if (teamspeakUUID != null && !teamspeakUUID.isEmpty()) {
            managerService.addTeamspeakService(teamspeakUUID, user.getName(), managerService.getTeamspeakName(teamspeakUUID));
        }
        if (discordUUID != null && !discordUUID.isEmpty()) {
            managerService.addDiscordService(discordUUID, user.getName(), managerService.getDiscordName(discordUUID));
        }
        model.addAttribute("msg", "A message has been pmed to the relevant service account");
        return "/profile/confirmservice";
    }

    @GetMapping("/profile/serviceDelete")
    public String deleteUserService(@RequestParam("uuid") int serviceID, @RequestParam("service") String serviceName, Authentication user, HttpServletRequest request) {

        if (serviceName.equalsIgnoreCase("discord")) {

            DiscordUser discordUser = userService.getDiscordById(serviceID);

            if (discordUser == null) {
                throw new ServiceProfileException("The requested account was not found");
            }
            if (!discordUser.getUsername().equalsIgnoreCase(user.getName()) && !request.isUserInRole(ADMIN_ROLE)) {
                throw new NotAuthorisedException("You cannot delete service profile accounts that you don't own");
            }
            userService.deleteDiscordUUID(discordUser);

        }else if (serviceName.equalsIgnoreCase("teamspeak")) {

            TeamspeakUser teamspeakUser = userService.getTeamspeakByID(serviceID);

            if (teamspeakUser == null) {
                throw new ServiceProfileException("The requested account was not found");
            }
            if (!teamspeakUser.getUsername().equalsIgnoreCase(user.getName()) && !request.isUserInRole(ADMIN_ROLE)) {
                throw new NotAuthorisedException("You cannot delete service profile accounts that you don't own");
            }
            userService.deleteTeamspeakUUID(teamspeakUser);
        }
        return "redirect:/profile";
    }
}
