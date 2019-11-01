package biz.oneilindustries.website.pojo;

import java.util.ArrayList;
import java.util.List;

public class CustomChannel {

    private String name;

    private String channelID;

    private int parentChannelID;

    private List<ServiceClient> usersInChannel;

    public CustomChannel(String name, String channelID, int parentChannelID) {
        this.name = name;
        if (name.contains("spacer")) {
            this.name = "";
        }
        this.channelID = channelID;
        this.parentChannelID = parentChannelID;
        this.usersInChannel = new ArrayList<>();
    }

    public void addClient(ServiceClient client) {
        this.usersInChannel.add(client);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public List<ServiceClient> getUsersInChannel() {
        return usersInChannel;
    }

    public void setUsersInChannel(List<ServiceClient> usersInChannel) {
        this.usersInChannel = usersInChannel;
    }

    public int getParentChannelID() {
        return parentChannelID;
    }

    public void setParentChannelID(int parentChannelID) {
        this.parentChannelID = parentChannelID;
    }
}
