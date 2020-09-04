package biz.oneilindustries.website.service;

import static biz.oneilindustries.website.config.AppConfig.FRONT_END_URL;

import biz.oneilindustries.services.discord.DiscordManager;
import biz.oneilindustries.services.teamspeak.TSManager;
import biz.oneilindustries.website.dao.ServiceTokenDAO;
import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.ServiceToken;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.exception.ServiceProfileException;
import biz.oneilindustries.website.pojo.CustomChannel;
import biz.oneilindustries.website.pojo.ServiceClient;
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    private static final String SERVICE_CONFIRMATION_MESSAGE = "This account has been registered on OneilIndustries.biz please confirm with this link " + FRONT_END_URL
        +"/services/confirm/";
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

    @Transactional
    public DiscordUser addDiscordService(String user, ServiceClient serviceClient) {

        DiscordUser exists = userService.getDiscordUUID(serviceClient.getUuid());

        if (exists != null) {
            throw new ServiceProfileException(ACCOUNT_ALREADY_REGISTERED);
        }
        DiscordUser discordUser = new DiscordUser(user, serviceClient.getUuid(), serviceClient.getName());
        userService.saveUserDiscordProfile(discordUser);

        ServiceToken serviceToken = new ServiceToken(UUID.randomUUID().toString(), serviceClient.getUuid(),"discord");
        saveServiceToken(serviceToken);

        sendDiscordPrivateMessage(serviceClient.getUuid(), SERVICE_CONFIRMATION_MESSAGE + serviceToken.getTokenUUID() + CLAIMED_BY + user);

        return discordUser;
    }

    @Transactional
    public TeamspeakUser addTeamspeakService(String user, ServiceClient serviceClient) {

        TeamspeakUser exists = userService.getTeamspeakUUID(serviceClient.getUuid());

        if (exists != null) {
            throw new ServiceProfileException(ACCOUNT_ALREADY_REGISTERED);
        }
        TeamspeakUser teamspeakUser = new TeamspeakUser(user, serviceClient.getUuid(), serviceClient.getName());
        userService.saveUserTeamspeakProfile(teamspeakUser);

        ServiceToken serviceToken = new ServiceToken(UUID.randomUUID().toString(), serviceClient.getUuid(),"teamspeak");
        saveServiceToken(serviceToken);

        sendTeamspeakPrivateMessage(serviceClient.getUuid(), SERVICE_CONFIRMATION_MESSAGE + serviceToken.getTokenUUID() + CLAIMED_BY + user);

        return teamspeakUser;
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
