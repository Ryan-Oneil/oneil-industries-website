package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.ServiceToken;
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
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

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

    @GetMapping("public/confirm")
    public ResponseEntity confirmService(@RequestParam("token") String token) {

        ServiceToken serviceToken = managerService.getServicetoken(token);

        if (serviceToken == null) {
            return ResponseEntity.badRequest().body("Invalid Token");
        }
        managerService.confirmService(serviceToken);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Admin services API
    @PostMapping("/teamspeak/admin/kickuser")
    public ResponseEntity kickUserTeamspeak(@RequestParam String userUUID, @RequestParam String reason) {

        managerService.kickUserFromServerTeamspeak(userUUID, reason);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/teamspeak/admin/bans")
    public List<Ban> getTeamspeakBanList() {
        return managerService.getTeamspeakBans();
    }

    @GetMapping("/teamspeak/admin/servergroups")
    public List<ServerGroup> getTeamspeakServerGroups() {
        return managerService.getTeamspeakServerGroups();
    }

    @PostMapping("/teamspeak/admin/banuser")
    public ResponseEntity banUserFromTeamspeak(@RequestParam String userUUID,
        @RequestParam long time, @RequestParam String reason) {

        managerService.banUserFromServerTeamspeak(userUUID, time, reason);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
