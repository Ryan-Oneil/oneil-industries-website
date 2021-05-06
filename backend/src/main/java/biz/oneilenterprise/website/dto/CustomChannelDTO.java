package biz.oneilenterprise.website.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomChannelDTO {

    private String title;
    private String key;
    private List<CustomChannelDTO> children;
    private List<ServiceClientDTO> usersInChannel;

    public CustomChannelDTO(String title, String key) {
        this.title = title;
        if (title.contains("spacer")) {
            this.title = "";
        }
        this.key = key;
        this.children = new ArrayList<>();
        this.usersInChannel = new ArrayList<>();
    }

    public void addClient(ServiceClientDTO client) {
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

    public List<ServiceClientDTO> getUsersInChannel() {
        return usersInChannel;
    }

    public void setUsersInChannel(List<ServiceClientDTO> usersInChannel) {
        this.usersInChannel = usersInChannel;
    }

    public List<CustomChannelDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CustomChannelDTO> children) {
        this.children = children;
    }

    public void addChild(CustomChannelDTO customChannelDTO) {
        this.children.add(customChannelDTO);
    }
}
