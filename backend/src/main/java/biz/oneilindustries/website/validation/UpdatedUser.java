package biz.oneilindustries.website.validation;

import biz.oneilindustries.website.entity.Quota;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class UpdatedUser {

    @NotNull
    private String username;

    @Null
    private String password;

    @NotNull
    private String email;

    private String role;

    private Integer enabled;

    private Quota quota;

    public UpdatedUser(@NotNull String username, String password, @NotNull String email, String role, Integer enabled,
        Quota quota) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.quota = quota;
    }

    public UpdatedUser(@NotNull String username, @NotNull String email, String role, Integer enabled) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }

    public UpdatedUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Quota getQuota() {
        return quota;
    }

    public void setQuota(Quota quota) {
        this.quota = quota;
    }
}
