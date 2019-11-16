package biz.oneilindustries.website.entity;

import biz.oneilindustries.website.entity.supers.Service;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "teamspeak_user")
public class TeamspeakUser extends Service {

    public TeamspeakUser(String username, String uuid, String serviceUsername) {
        super(username, uuid, serviceUsername);
    }

    public TeamspeakUser() {
    }
}
