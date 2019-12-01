package biz.oneilindustries.website.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "username")
    private String username;

    private String password;

    private boolean enabled;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "username", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true  )
    private List<Authority> customAuthorities;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Quota storeQuota;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, boolean enabled, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
    }

    public User(String username, String password, boolean enabled, String email, Quota storeQuota) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
        this.storeQuota = storeQuota;
    }

    public User(String username, String password, boolean enabled, String email,
        List<Authority> customAuthorities, Quota storeQuota) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
        this.customAuthorities = customAuthorities;
        this.storeQuota = storeQuota;
    }

    public User() {
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // add user's authorities
        for (Authority authority : customAuthorities) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }

    public void addAuthority(Authority authority) {
        if (this.customAuthorities == null) {
            this.customAuthorities = new ArrayList<>();
        }
        this.customAuthorities.add(authority);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Authority> getCustomAuthorities() {
        return customAuthorities;
    }

    public void setCustomAuthorities(List<Authority> customAuthorities) {
        this.customAuthorities = customAuthorities;
    }

    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", enabled=" + enabled +
            ", email='" + email + '\'' +
            '}';
    }

    public Quota getStoreQuota() {
        return storeQuota;
    }

    public void setStoreQuota(Quota storeQuota) {
        this.storeQuota = storeQuota;
    }
}
