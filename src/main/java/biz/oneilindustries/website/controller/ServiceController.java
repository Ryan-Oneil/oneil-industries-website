package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.ServiceToken;
import biz.oneilindustries.website.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/services/confirm")
    public String confirmService(@RequestParam("token") String token, Model model) {

        ServiceToken serviceToken = managerService.getServicetoken(token);

        if (serviceToken == null) {
            model.addAttribute("msg","Invalid token");
            return "/services/confirm";
        }
        managerService.confirmService(serviceToken);

        model.addAttribute("msg", serviceToken.getService() + " account has been confirmed and approved");
        return "/services/confirm";
    }
}
