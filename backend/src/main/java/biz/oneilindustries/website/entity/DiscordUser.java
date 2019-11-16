package biz.oneilindustries.website.entity;

import biz.oneilindustries.website.entity.supers.Service;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "discord_user")
public class DiscordUser extends Service {

    public DiscordUser(String username, String uuid, String serviceUsername) {
        super(username, uuid, serviceUsername);
    }

    public DiscordUser() {
    }
}
