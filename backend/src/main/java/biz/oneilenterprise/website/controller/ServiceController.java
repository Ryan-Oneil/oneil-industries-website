package biz.oneilenterprise.website.controller;

import biz.oneilenterprise.website.dto.CustomChannelDTO;
import biz.oneilenterprise.website.service.ManagerService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ManagerService managerService;

    public ServiceController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("public/teamspeak")
    public List<CustomChannelDTO> getActiveTeamspeakChannels() {
        return managerService.getTeamspeakChannelUsers();
    }

    @GetMapping("public/discord")
    public List<CustomChannelDTO> getActiveDiscordChannels() {
        return managerService.getDiscordCategories();
    }

}
