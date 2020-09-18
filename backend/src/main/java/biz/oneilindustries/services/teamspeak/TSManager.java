package biz.oneilindustries.services.teamspeak;

import biz.oneilindustries.rank.Rank;
import biz.oneilindustries.website.pojo.CustomChannel;
import biz.oneilindustries.website.pojo.ServiceClient;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TSManager {

    private TS3Api ts3Api;

    public TSManager() {
        this.ts3Api = TSBot.api();
    }

    public List<Client> getOnlineUsers() {
        this.ts3Api = TSBot.api();

        return ts3Api.getClients();
    }

    public String getUsername(String uuid) {
        this.ts3Api = TSBot.api();

         return ts3Api.getClientByUId(uuid).getNickname();
    }

    public List<Channel> getServerChannels() {
        this.ts3Api = TSBot.api();

        return ts3Api.getChannels();
    }

    public List<CustomChannel> getChannelsMapped() {
        List<Channel> channels = getServerChannels();
        List<Client> clients = getOnlineUsers();

        ArrayList<CustomChannel> customChannels = new ArrayList<>();

        channels.forEach(channel -> {
            if (channel.getParentChannelId() == 0) {
                customChannels.add(recursiveTSChannelMapping(channels, null, channel, clients));
            }
        });
        return customChannels;
    }

    private CustomChannel recursiveTSChannelMapping(List<Channel> channelList, CustomChannel parentChannel, Channel childChannel, List<Client> clients) {
        CustomChannel child = new CustomChannel(childChannel.getName(), "" + childChannel.getId());
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

    private List<ServiceClient> getTSClientsForChannel(int channelID, List<Client> clients) {
        return clients.stream()
            .filter(client -> client.getChannelId() == channelID)
            .map(client -> new ServiceClient(client.getNickname(), client.getUniqueIdentifier()))
            .collect(Collectors.toList());
    }

    public List<ServerGroup> getServerGroups() {

        this.ts3Api = TSBot.api();

        return ts3Api.getServerGroups();
    }

    public void sendPrivateMessage(String uuid, String message) {
        this.ts3Api = TSBot.api();

        ts3Api.sendPrivateMessage(ts3Api.getClientByUId(uuid).getId(), message);
    }

    public void pokeUser(String uuid, String message) {
        this.ts3Api = TSBot.api();
        ts3Api.pokeClient(ts3Api.getDatabaseClientByUId(uuid).getDatabaseId(), message);
    }

    public void addUserRole(String uuid, String roleName) {
        this.ts3Api = TSBot.api();

        Rank.setTeamspeakServerRoles(ts3Api.getServerGroups());
        ServerGroup serverGroup = Rank.getRequiredTeamspeakRole(roleName);

        ts3Api.addClientToServerGroup(serverGroup.getId(), ts3Api.getClientByUId(uuid).getDatabaseId());
    }

    public void removeUserRoles(String uuid) {
        this.ts3Api = TSBot.api();

        int[] roles = ts3Api.getClientByUId(uuid).getServerGroups();

        for (int role : roles) {
            ts3Api.removeClientFromServerGroup(role, ts3Api.getClientByUId(uuid).getDatabaseId());
        }
    }

    public void kickUser(String uuid, String reason) {
        this.ts3Api = TSBot.api();
        ts3Api.kickClientFromServer(reason, ts3Api.getDatabaseClientByUId(uuid).getDatabaseId());
    }

    public void banUser(String uuid, long time, String reason) {
        this.ts3Api = TSBot.api();
        ts3Api.banClient(ts3Api.getDatabaseClientByUId(uuid).getDatabaseId(), time, reason);
    }

    public void unbanUser(int banID) {
        this.ts3Api = TSBot.api();
        ts3Api.deleteBan(banID);
    }

    public List<Ban> getBanList() {
        this.ts3Api = TSBot.api();
        return ts3Api.getBans();
    }

    public void moveUserToChannel(String uuid, int channelID) {
        this.ts3Api = TSBot.api();
        ts3Api.moveClient(ts3Api.getDatabaseClientByUId(uuid).getDatabaseId(), channelID);
    }
}
