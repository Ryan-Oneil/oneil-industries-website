package biz.oneilindustries.website.service;

import biz.oneilindustries.services.discord.DiscordManager;
import biz.oneilindustries.services.teamspeak.TSManager;
import biz.oneilindustries.website.pojo.CustomChannel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.List;
import net.dv8tion.jda.api.entities.Member;
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
        return tsManager.getChannelsMapped();
    }

    public List<CustomChannel> getDiscordCategories() {
        return discordManager.getChannelsMapped();
    }

    public List<Member> getDiscordMembers() {
        return discordManager.getMembers();
    }

    public void sendDiscordPrivateMessage(String uuid, String message) {
        discordManager.sendUserMessage(uuid, message);
    }

    public void sendTeamspeakPrivateMessage(String uuid, String message) {
        tsManager.sendPrivateMessage(uuid, message);
    }

    public String getDiscordName(String uuid) {
        return discordManager.getUserName(uuid);
    }

    public String getTeamspeakName(String uuid) {
        return tsManager.getUsername(uuid);
    }

    public void addDiscordRole(String uuid, String role) {
        discordManager.addUserRole(discordManager.getMember(uuid), role);
    }

    public void deleteDiscordRoles(String uuid) {
        discordManager.removeRoles(discordManager.getMember(uuid));
    }

    // Teamspeak services methods

    public void pokeTeamspeakUser(String uuid, String message) {
        tsManager.pokeUser(uuid, message);
    }

    public List<ServerGroup> getTeamspeakServerGroups() {
        return tsManager.getServerGroups();
    }

    public void addTeamspeakRole(String uuid, String role) {
        tsManager.addUserRole(uuid, role);
    }

    public void deleteTeamspeakRoles(String uuid) {
        tsManager.removeUserRoles(uuid);
    }

    public void kickUserFromServerTeamspeak(String uuid, String reason) {
        tsManager.kickUser(uuid, reason);
    }

    public void banUserFromServerTeamspeak(String uuid, long time, String reason) {
        tsManager.banUser(uuid, time, reason);
    }

    public void unBanUserFromTeamspeak(int banID) {
        tsManager.unbanUser(banID);
    }

    public List<Ban> getTeamspeakBans() {
        return tsManager.getBanList();
    }

    public void moveTeamspeakUserToChannel(String uuid, int channelID) {
        tsManager.moveUserToChannel(uuid, channelID);
    }
}
