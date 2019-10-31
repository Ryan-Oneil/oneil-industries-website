package biz.oneilindustries.website.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "authorities")
public class Authority implements GrantedAuthority {


    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "username")
    @Id
    private int id;

    private String username;

    private String authority;

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public Authority() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority1 = (Authority) o;
        return Objects.equals(getUsername(), authority1.getUsername()) &&
                getAuthority().equals(authority1.getAuthority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAuthority());
    }
}
