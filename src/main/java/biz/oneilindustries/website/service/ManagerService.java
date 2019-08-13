package biz.oneilindustries.website.service;

import biz.oneilindustries.services.discord.DiscordManager;
import biz.oneilindustries.services.teamspeak.CustomChannel;
import biz.oneilindustries.services.teamspeak.TSManager;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Category;
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

    public List<Client> getTSClients() {
        return tsManager.getOnlineUsers();
    }

    public List<CustomChannel> getTeamspeakChannelUsers() {

        List<Channel> channels = tsManager.getServerChannels();

        List<Client> clients = tsManager.getOnlineUsers();

        ArrayList<CustomChannel> customChannels = new ArrayList<>();

        for (Channel channel : channels) {

            CustomChannel customChannel = new CustomChannel(channel.getName(), channel.getId(), channel.getParentChannelId());

            if (!channel.isEmpty()) {
                for (Client client : clients) {
                    if (client.getChannelId() == customChannel.getChannelID()) {
                        customChannel.addClient(client);
                    }
                }
            }
            customChannels.add(customChannel);
        }
        return customChannels;
    }

    public List<Category> getDiscordCategories() {
        return discordManager.getCategories();
    }
}
