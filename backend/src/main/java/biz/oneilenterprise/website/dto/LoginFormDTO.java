package biz.oneilenterprise.website.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginFormDTO {

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NotNull
    private String password;

    @NotNull
    @Email
    private String email;

    public LoginFormDTO(@NotNull @Size(min = 2, max = 30) String name, @NotNull String password, @NotNull String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public LoginFormDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
