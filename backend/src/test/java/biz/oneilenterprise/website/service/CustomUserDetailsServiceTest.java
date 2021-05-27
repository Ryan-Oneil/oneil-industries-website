package biz.oneilenterprise.website.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.exception.TooManyLoginAttempts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CustomUserDetailsServiceTest extends BaseIntegrationTest {

    private static final String CLIENT_IP = "0.0.0.0";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Test
    public void loadUserByUsernameTest() {
        User user = (User) customUserDetailsService.loadUserByUsername("test@example.com");

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void loadUserByUsernameNotFoundTest() {
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("notfound"))
            .isExactlyInstanceOf(UsernameNotFoundException.class)
            .hasMessage("notfound doesn't exist");
    }

    @Test
    public void loadUserByUsernameBlockedTest() {
        setupRequestIP();

        for (int i = 0; i < 5; i++) {
            loginAttemptService.loginFailed(CLIENT_IP);
        }
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("test"))
            .isExactlyInstanceOf(TooManyLoginAttempts.class)
            .hasMessage("Too many login attempts. Try again in a few minutes");
    }

    @Test
    public void getClientIPTest() {
        setupRequestIP();

        String localIp = customUserDetailsService.getClientIP();

        assertThat(localIp).isEqualTo(CLIENT_IP);
    }

    public void setupRequestIP() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(CLIENT_IP);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
}
