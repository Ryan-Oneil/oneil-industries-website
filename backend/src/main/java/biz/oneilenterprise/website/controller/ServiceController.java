package biz.oneilenterprise.website.controller;

import biz.oneilenterprise.website.pojo.CustomChannel;
import biz.oneilenterprise.website.service.ManagerService;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ManagerService managerService;

    public ServiceController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("public/teamspeak")
    public List<CustomChannel> getActiveTeamspeakChannels() {
        return managerService.getTeamspeakChannelUsers();
    }

    @GetMapping("public/discord")
    public List<CustomChannel> getActiveDiscordChannels() {
        return managerService.getDiscordCategories();
    }

    //Admin voiceservices API
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
