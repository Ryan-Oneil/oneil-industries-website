package biz.oneilindustries.services.teamspeak;

import biz.oneilindustries.Rank.Rank;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.List;
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
