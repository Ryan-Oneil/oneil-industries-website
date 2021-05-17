package biz.oneilenterprise.website.voiceservices.teamspeak;

import biz.oneilenterprise.website.dto.CustomChannelDTO;
import biz.oneilenterprise.website.dto.ServiceClientDTO;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TSManager {

    private final TS3Api ts3Api;

    public TSManager(TS3Api ts3Api) {
        this.ts3Api = ts3Api;
    }

    public List<Client> getOnlineUsers() {
        return ts3Api.getClients();
    }

    public String getUsername(String uuid) {
         return ts3Api.getClientByUId(uuid).getNickname();
    }

    public List<Channel> getServerChannels() {
        return ts3Api.getChannels();
    }

    public List<CustomChannelDTO> getChannelsMapped() {
        List<Channel> channels = getServerChannels();
        List<Client> clients = getOnlineUsers();

        ArrayList<CustomChannelDTO> customChannelDTOS = new ArrayList<>();

        channels.forEach(channel -> {
            if (channel.getParentChannelId() == 0) {
                customChannelDTOS.add(recursiveTSChannelMapping(channels, null, channel, clients));
            }
        });
        return customChannelDTOS;
    }

    private CustomChannelDTO recursiveTSChannelMapping(List<Channel> channelList, CustomChannelDTO parentChannel, Channel childChannel, List<Client> clients) {
        CustomChannelDTO child = new CustomChannelDTO(childChannel.getName(), "" + childChannel.getId());
        child.setUsersInChannel(getTSClientsForChannel(childChannel.getId(), clients));

        if (parentChannel != null) {
            parentChannel.addChild(child);
        }
        List<Channel> childChannels = channelList.stream().filter(channel -> channel.getParentChannelId() == childChannel.getId()).collect(Collectors.toList());

        if (!childChannels.isEmpty()) {
            childChannels.forEach(channel -> recursiveTSChannelMapping(channelList, child, channel, clients));
        }
        return child;
    }

    private List<ServiceClientDTO> getTSClientsForChannel(int channelID, List<Client> clients) {
        return clients.stream()
            .filter(client -> client.getChannelId() == channelID && client.isRegularClient())
            .map(client -> new ServiceClientDTO(client.getNickname(), client.getUniqueIdentifier()))
            .collect(Collectors.toList());
    }
    
}
