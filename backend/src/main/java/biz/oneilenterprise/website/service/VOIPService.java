package biz.oneilenterprise.website.service;

import biz.oneilenterprise.website.dto.CustomChannelDTO;
import biz.oneilenterprise.website.voiceservices.discord.DiscordManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VOIPService {

    private final DiscordManager discordManager;

    @Autowired
    public VOIPService(DiscordManager discordManager) {
        this.discordManager = discordManager;
    }

    public List<CustomChannelDTO> getDiscordCategories() {
        return discordManager.getChannelsMapped();
    }

}
