package biz.oneilindustries.website.validation;

import java.util.Optional;
import javax.validation.constraints.NotNull;

public class UpdatedUser {

    @NotNull
    private String username;

    private String password;

    @NotNull
    private String email;

    private String role;

    private Boolean enabled;

    public UpdatedUser(@NotNull String username, String password, @NotNull String email, String role, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }

    public UpdatedUser(@NotNull String username, @NotNull String email, String role, Boolean enabled) {
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

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public  Optional<String> getRole() {
        return Optional.ofNullable(role);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public  Optional<Boolean> getEnabled() {
        return Optional.ofNullable(enabled);
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
