package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceController {

    private final ManagerService managerService;

    @Autowired
    public ServiceController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/services")
    public String showServiceHome(Model model) {

        model.addAttribute("teamspeakChannels", managerService.getTeamspeakChannelUsers());
        model.addAttribute("discordCategories", managerService.getDiscordCategories());

        return "services/service";
    }
}
