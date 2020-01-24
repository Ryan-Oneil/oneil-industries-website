package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.ServiceToken;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.pojo.CustomChannel;
import biz.oneilindustries.website.pojo.ServiceClient;
import biz.oneilindustries.website.service.ManagerService;
import biz.oneilindustries.website.service.UserService;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final ManagerService managerService;
    private final UserService userService;

    @Autowired
    public ServiceController(ManagerService managerService, UserService userService) {
        this.managerService = managerService;
        this.userService = userService;
    }

    @GetMapping("public/teamspeak")
    public List<CustomChannel> getActiveTeamspeakChannels() {
        return managerService.getTeamspeakChannelUsers();
    }

    @GetMapping("public/discord")
    public List<CustomChannel> getActiveDiscordChannels() {
        return managerService.getDiscordCategories();
    }

    @GetMapping("/public/unregistered")
    public ResponseEntity getUnRegisteredServerClients() {

        HashMap<String, Object> unRegisteredClients = new HashMap<>();

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
        unRegisteredClients.put("teamspeakUsers", tsClients);
        unRegisteredClients.put("discordUsers", discordClients);

        return ResponseEntity.ok(unRegisteredClients);
    }

    @PostMapping("/user/addservice/{service}")
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

    @DeleteMapping("/user/teamspeak/delete/{id}")
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

    @DeleteMapping("/user/discord/delete/{id}")
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

    @PostMapping("/user/confirm/{token}")
    public ResponseEntity confirmService(@PathVariable String token) {

        ServiceToken serviceToken = managerService.getServicetoken(token);

        if (serviceToken == null) {
            return ResponseEntity.badRequest().body("Invalid Token");
        }
        managerService.confirmService(serviceToken);

        return ResponseEntity.ok("Service client has been verified!");
    }

    //Admin services API
    @PostMapping("/admin/teamspeak/kickuser")
    public ResponseEntity kickUserTeamspeak(@RequestParam String userUUID, @RequestParam String reason) {

        managerService.kickUserFromServerTeamspeak(userUUID, reason);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/admin/teamspeak/bans")
    public List<Ban> getTeamspeakBanList() {
        return managerService.getTeamspeakBans();
    }

    @GetMapping("/admin/teamspeak/servergroups")
    public List<ServerGroup> getTeamspeakServerGroups() {
        return managerService.getTeamspeakServerGroups();
    }

    @PostMapping("/admin/teamspeak/banuser")
    public ResponseEntity banUserFromTeamspeak(@RequestParam String userUUID,
        @RequestParam long time, @RequestParam String reason) {

        managerService.banUserFromServerTeamspeak(userUUID, time, reason);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
