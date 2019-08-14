package biz.oneilindustries.website.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "teamspeak_user")
public class TeamspeakUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String uuid;

    @Column(name = "teamspeak_name")
    private String teamspeakName;

    private int activated;

    public TeamspeakUser(String username, String uuid, String teamspeakName) {
        this.username = username;
        this.uuid = uuid;
        this.teamspeakName = teamspeakName;
    }

    public TeamspeakUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTeamspeakName() {
        return teamspeakName;
    }

    public void setTeamspeakName(String teamspeakName) {
        this.teamspeakName = teamspeakName;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }
}
