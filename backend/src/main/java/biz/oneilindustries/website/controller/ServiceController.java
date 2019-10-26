package biz.oneilindustries.website.controller;

import biz.oneilindustries.services.teamspeak.CustomChannel;
import biz.oneilindustries.website.entity.ServiceToken;
import biz.oneilindustries.website.service.ManagerService;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.List;
import net.dv8tion.jda.api.entities.Category;
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

    @Autowired
    public ServiceController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/teamspeak")
    public List<CustomChannel> getActiveTeamspeakChannels() {
        return managerService.getTeamspeakChannelUsers();
    }

    @GetMapping("/discord")
    public List<Category> getActiveDiscordChannels() {
        return managerService.getDiscordCategories();
    }

    @GetMapping("/confirm")
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
