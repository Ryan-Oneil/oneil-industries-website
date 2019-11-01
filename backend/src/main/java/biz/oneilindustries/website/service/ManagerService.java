package biz.oneilindustries.website.service;

import biz.oneilindustries.services.discord.DiscordManager;
import biz.oneilindustries.website.pojo.CustomChannel;
import biz.oneilindustries.services.teamspeak.TSManager;
import biz.oneilindustries.website.dao.ServiceTokenDAO;
import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.ServiceToken;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.exception.ServiceProfileException;
import biz.oneilindustries.website.pojo.ServiceClient;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    private static final String SERVICE_CONFIRMATION_MESSAGE = "This account has been registered on OneilIndustries.biz please confirm with this link http://oneilindustries.biz/services/confirm?token=";
    private static final String ACCOUNT_ALREADY_REGISTERED = "This account is already registered to another user";
    private static final String CLAIMED_BY = "\nClaimed by ";

    private final TSManager tsManager;

    private final DiscordManager discordManager;

    private final UserService userService;

    private final ServiceTokenDAO serviceTokenDAO;

    @Autowired
    public ManagerService(TSManager tsManager, DiscordManager discordManager, UserService userService,
        ServiceTokenDAO serviceTokenDAO) {
        this.tsManager = tsManager;
        this.discordManager = discordManager;
        this.userService = userService;
        this.serviceTokenDAO = serviceTokenDAO;
    }

    public List<Client> getTSClients() {
        return tsManager.getOnlineUsers();
    }

    public List<CustomChannel> getTeamspeakChannelUsers() {

        List<Channel> channels = tsManager.getServerChannels();
        List<Client> clients = tsManager.getOnlineUsers();

        ArrayList<CustomChannel> customChannels = new ArrayList<>();

        for (Channel channel : channels) {

            CustomChannel customChannel = new CustomChannel(channel.getName(), "" + channel.getId(), channel.getParentChannelId());

            if (!channel.isEmpty()) {
                for (Client client : clients) {
                    if (customChannel.getChannelID().equalsIgnoreCase(client.getChannelId() + "")) {
                        customChannel.addClient(new ServiceClient(client.getNickname(), client.getUniqueIdentifier()));
                    }
                }
            }
            customChannels.add(customChannel);
        }
        return customChannels;
    }

    public List<CustomChannel> getDiscordCategories() {
        List<CustomChannel> discordChannels = new ArrayList<>();

        //Inefficient but sadly I can't just sent the api wrapper's objects due to jackson and mapping issues
        for (Category category: discordManager.getCategories()) {
            for (VoiceChannel channel: category.getVoiceChannels()) {
                CustomChannel discordChannel = new CustomChannel(channel.getName(), channel.getId(), 0);
                for (Member member: channel.getMembers()) {
                    discordChannel.addClient(new ServiceClient(member.getEffectiveName(), member.getId()));
                }
                discordChannels.add(discordChannel);
            }
        }
        return discordChannels;
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

    @Transactional
    public void addDiscordService(String uuid, String user, String discordName) {

        DiscordUser exists = userService.getDiscordUUID(uuid);

        if (exists != null) {
            throw new ServiceProfileException(ACCOUNT_ALREADY_REGISTERED);
        }
        DiscordUser discordUser = new DiscordUser(user, uuid, discordName);
        userService.saveUserDiscordProfile(discordUser);

        ServiceToken serviceToken = new ServiceToken(UUID.randomUUID().toString(), uuid,"discord");
        saveServiceToken(serviceToken);

        sendDiscordPrivateMessage(uuid, SERVICE_CONFIRMATION_MESSAGE + serviceToken.getTokenUUID() + CLAIMED_BY + user);
    }

    @Transactional
    public void addTeamspeakService(String uuid, String user, String teamspeakName) {

        TeamspeakUser exists = userService.getTeamspeakUUID(uuid);

        if (exists != null) {
            throw new ServiceProfileException(ACCOUNT_ALREADY_REGISTERED);
        }
        TeamspeakUser teamspeakUser = new TeamspeakUser(user, uuid, teamspeakName);
        userService.saveUserTeamspeakProfile(teamspeakUser);

        ServiceToken serviceToken = new ServiceToken(UUID.randomUUID().toString(), uuid,"teamspeak");
        saveServiceToken(serviceToken);

        sendTeamspeakPrivateMessage(uuid, SERVICE_CONFIRMATION_MESSAGE + serviceToken.getTokenUUID() + CLAIMED_BY + user);
    }

    @Transactional
    public ServiceToken getServicetoken(String uuid) {
        return serviceTokenDAO.getToken(uuid);
    }

    @Transactional
    public void saveServiceToken(ServiceToken serviceToken) {
        serviceTokenDAO.savetoken(serviceToken);
    }

    @Transactional
    public void deleteServiceToken(ServiceToken serviceToken) {
        serviceTokenDAO.deleteToken(serviceToken);
    }

    @Transactional
    public void confirmService(ServiceToken token) {

        if (token.getService().equalsIgnoreCase("discord")) {
            DiscordUser discordUser = userService.getDiscordUUID(token.getServicUUID());
            discordUser.setActivated(1);
            userService.saveUserDiscordProfile(discordUser);
        }else {
            TeamspeakUser teamspeakUser = userService.getTeamspeakUUID(token.getServicUUID());
            teamspeakUser.setActivated(1);
            userService.saveUserTeamspeakProfile(teamspeakUser);
        }
        deleteServiceToken(token);
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
