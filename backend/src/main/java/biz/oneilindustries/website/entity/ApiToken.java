package biz.oneilindustries.website.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_access_tokens")
public class ApiToken {

    @Id
    private String username;

    private String token;

    public ApiToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public ApiToken() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
