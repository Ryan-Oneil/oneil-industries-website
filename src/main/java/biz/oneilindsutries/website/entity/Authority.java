package biz.oneilindsutries.website.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User username;

    private String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    public Authority() {
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


}
