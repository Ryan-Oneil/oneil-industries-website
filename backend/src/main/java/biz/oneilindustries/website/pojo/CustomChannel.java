package biz.oneilindustries.website.pojo;

import java.util.ArrayList;
import java.util.List;

public class CustomChannel {

    private String title;
    private String key;
    private List<CustomChannel> children;
    private List<ServiceClient> usersInChannel;

    public CustomChannel(String title, String key) {
        this.title = title;
        if (title.contains("spacer")) {
            this.title = "";
        }
        this.key = key;
        this.children = new ArrayList<>();
        this.usersInChannel = new ArrayList<>();
    }

    public void addClient(ServiceClient client) {
        this.usersInChannel.add(client);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<ServiceClient> getUsersInChannel() {
        return usersInChannel;
    }

    public void setUsersInChannel(List<ServiceClient> usersInChannel) {
        this.usersInChannel = usersInChannel;
    }

    public List<CustomChannel> getChildren() {
        return children;
    }

    public void setChildren(List<CustomChannel> children) {
        this.children = children;
    }

    public void addChild(CustomChannel customChannel) {
        this.children.add(customChannel);
    }
}
