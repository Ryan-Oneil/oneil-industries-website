package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.exception.NotAuthorisedException;
import biz.oneilindustries.website.exception.ServiceProfileException;
import biz.oneilindustries.website.service.ManagerService;
import biz.oneilindustries.website.service.UserService;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserProfileController {

    private final UserService userService;
    private final ManagerService managerService;

    @Autowired
    public UserProfileController(UserService userService, ManagerService managerService) {
        this.userService = userService;
        this.managerService = managerService;
    }

    @GetMapping("/profile")
    public String showUserProfileHome(Authentication user, Model model) {

        model.addAttribute("user", userService.getUser(user.getName()));
        model.addAttribute("userDiscord", userService.getUserDiscordProfiles(user.getName()));
        model.addAttribute("userTeamspeak", userService.getUserTeamspeakProfile(user.getName()));

        List<Client> tsClients = new ArrayList<>();
        List<String> registeredClients = userService.getTeamspeakUUIDs();

        for (Client client : managerService.getTSClients()) {
            if (!registeredClients.contains(client.getUniqueIdentifier())) {
                tsClients.add(client);
            }
        }

        registeredClients = userService.getDiscordUUIDs();
        List<Member> discordClients = new ArrayList<>();

        for (Member member : managerService.getDiscordMembers()) {
            if (!registeredClients.contains(member.getId())) {
                discordClients.add(member);
            }
        }

        model.addAttribute("teamspeakUsers", tsClients);
        model.addAttribute("discordUsers", discordClients);

        return "profile/profile";
    }

    @PostMapping("/profile/update")
    public String updateUserDetails(@RequestParam("email") String email, @RequestParam(value = "password",required = false) String password, Authentication authentication) {

        User user = userService.getUser(authentication.getName());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        User checkEmail = userService.getUserByEmail(email);

        if ( checkEmail != null && !email.equalsIgnoreCase(user.getEmail())) {
            return "profile";
        }
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            userService.changeUserPassword(user,password);
        }
        userService.saveUser(user);

        return "redirect:/profile";
    }

    @PostMapping("/profile/servicesAdd")
    public String addUserService(@RequestParam(value = "teamspeak", required = false) String teamspeakUUID, @RequestParam(value = "discord", required = false) String discordUUID, Authentication user) {

        if (teamspeakUUID != null && !teamspeakUUID.isEmpty()) {
            managerService.addTeamspeakService(teamspeakUUID, user.getName(), managerService.getTeamspeakName(teamspeakUUID));
        }
        if (discordUUID != null && !discordUUID.isEmpty()) {
            managerService.addDiscordService(discordUUID, user.getName(), managerService.getDiscordName(discordUUID));
        }
        return "/profile/confirmservice";
    }

    @GetMapping("/profile/serviceDelete")
    public String deleteUserService(@RequestParam("uuid") String serviceUUID, @RequestParam("service") String serviceName, Authentication user) {

        if (serviceName.equalsIgnoreCase("discord")) {

            DiscordUser discordUser = userService.getDiscordUUID(serviceUUID);

            if (discordUser == null) {
                throw new ServiceProfileException("The requested account was not found");
            }
            if (!discordUser.getUsername().equalsIgnoreCase(user.getName())) {
                throw new NotAuthorisedException("You cannot delete service profile accounts that you don't own");
            }
            userService.deleteDiscordUUID(discordUser);

        }else if (serviceName.equalsIgnoreCase("teamspeak")) {

            TeamspeakUser teamspeakUser = userService.getTeamspeakUUID(serviceUUID);

            if (teamspeakUser == null) {
                throw new ServiceProfileException("The requested account was not found");
            }
            if (!teamspeakUser.getUsername().equalsIgnoreCase(user.getName())) {
                throw new NotAuthorisedException("You cannot delete service profile accounts that you don't own");
            }
            userService.deleteTeamspeakUUID(teamspeakUser);
        }

        return "redirect:/profile";
    }
}
