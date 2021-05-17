package biz.oneilenterprise.website.service;

import biz.oneilenterprise.website.dto.CustomChannelDTO;
import biz.oneilenterprise.website.voiceservices.discord.DiscordManager;
import biz.oneilenterprise.website.voiceservices.teamspeak.TSManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    private final TSManager tsManager;

    private final DiscordManager discordManager;

    @Autowired
    public ManagerService(TSManager tsManager, DiscordManager discordManager) {
        this.tsManager = tsManager;
        this.discordManager = discordManager;
    }

    public List<CustomChannelDTO> getTeamspeakChannelUsers() {
        return tsManager.getChannelsMapped();
    }

    public List<CustomChannelDTO> getDiscordCategories() {
        return discordManager.getChannelsMapped();
    }

}
