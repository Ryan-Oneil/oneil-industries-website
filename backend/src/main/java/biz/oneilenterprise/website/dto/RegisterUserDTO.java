package biz.oneilenterprise.website.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterUserDTO {

    @NotNull
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$", message = "Username may only contain a-Z . _")
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Email
    private String email;

    public RegisterUserDTO(
        @NotNull @Size(min = 2, max = 30) @Pattern(regexp = "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$", message = "Username may only contain a-Z . _") String username,
        @NotNull String password, @NotNull @Email String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public RegisterUserDTO() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
