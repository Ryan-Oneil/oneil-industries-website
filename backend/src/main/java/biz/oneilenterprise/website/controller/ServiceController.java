package biz.oneilenterprise.website.controller;

import biz.oneilenterprise.website.dto.CustomChannelDTO;
import biz.oneilenterprise.website.service.VOIPService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final VOIPService VOIPService;

    public ServiceController(VOIPService VOIPService) {
        this.VOIPService = VOIPService;
    }

    @GetMapping("public/discord")
    public List<CustomChannelDTO> getActiveDiscordChannels() {
        return VOIPService.getDiscordCategories();
    }

}
