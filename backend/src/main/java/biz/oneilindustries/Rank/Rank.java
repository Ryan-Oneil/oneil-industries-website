package biz.oneilindustries.Rank;

import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Role;

public class Rank {

    private static List<String> officerRanks;
    private static List<String> approvedRoles;
    private static List<Role> discordServerRoles;
    private static List<ServerGroup> teamspeakServerRoles;

    static {
        officerRanks = new ArrayList<>();
        //Defines what rank name is an officer
        officerRanks.add("ceo");
        officerRanks.add("coo");
        officerRanks.add("manager");
        officerRanks.add("server admin");

        //Defines what rank name can be given with the bot
        approvedRoles = new ArrayList<>();
        approvedRoles.add("zarp");
        approvedRoles.add("oneil");
        approvedRoles.add("member");
    }

    private Rank() {
    }

    public static boolean isOfficer(String name) {
        return officerRanks.contains(name.toLowerCase());
    }

    public static boolean isOfficer(List<String> roles) {
        boolean isAllowed = false;

        for (String role : roles) {
            if (officerRanks.contains(role)) {
                isAllowed = true;
                break;
            }
        }
        return isAllowed;
    }

    public static boolean isApprovedRole(String roleName) {
        return approvedRoles.contains(roleName);
    }

    public static void setDiscordServerRoles(List<Role> discordServerRoles) {
        Rank.discordServerRoles = discordServerRoles;
    }

    public static List<Role> getDiscordServerRoles() {
        return discordServerRoles;
    }

    public static Role getRequiredDiscordRole(String roleName) {
        //Checks if nothing was passed or if someone tries giving a non approved role
        if (discordServerRoles == null || discordServerRoles.isEmpty() || !approvedRoles.contains(roleName)) {
            return null;
        }

        for (Role role : discordServerRoles) {
            if (role.getName().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        return null;
    }

    public static List<ServerGroup> getTeamspeakServerRoles() {
        return teamspeakServerRoles;
    }

    public static void setTeamspeakServerRoles(List<ServerGroup> teamspeakServerRoles) {
        Rank.teamspeakServerRoles = teamspeakServerRoles;
    }

    public static ServerGroup getRequiredTeamspeakRole(String roleName) {

        //Checks if nothing was passed or if someone tries giving a non approved role
        if (teamspeakServerRoles == null || teamspeakServerRoles.isEmpty() || !approvedRoles.contains(roleName)) {
            return null;
        }

        for (ServerGroup serverGroup : teamspeakServerRoles) {
            if (serverGroup.getName().equalsIgnoreCase(roleName)) {
                return serverGroup;
            }
        }
        return null;
    }
}
