package biz.oneilindsutries.website.validation;

import javax.validation.constraints.NotNull;

public class UpdatedUser {

    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String role;
    @NotNull
    private int enabled;

    public UpdatedUser(@NotNull String username, @NotNull String email, @NotNull String role, @NotNull int enabled) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
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

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
}
