package biz.oneilindustries.services.teamspeak;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import java.util.ArrayList;
import java.util.List;

public class CustomChannel {

    private String name;

    private int channelID;

    private int parentChannelID;

    private List<Client> usersInChannel;

    public CustomChannel(String name, int channelID, int parentChannelID) {
        this.name = name;
        if (name.contains("spacer")) {
            this.name = "";
        }
        this.channelID = channelID;
        this.parentChannelID = parentChannelID;
        this.usersInChannel = new ArrayList<>();
    }

    public void addClient(Client client) {
        this.usersInChannel.add(client);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public List<Client> getUsersInChannel() {
        return usersInChannel;
    }

    public void setUsersInChannel(List<Client> usersInChannel) {
        this.usersInChannel = usersInChannel;
    }

    public int getParentChannelID() {
        return parentChannelID;
    }

    public void setParentChannelID(int parentChannelID) {
        this.parentChannelID = parentChannelID;
    }
}
