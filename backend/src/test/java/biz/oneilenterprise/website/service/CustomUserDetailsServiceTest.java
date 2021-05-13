package biz.oneilenterprise.website.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.exception.TooManyLoginAttempts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({"/testData.sql"})
@Sql(scripts = "/delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class CustomUserDetailsServiceTest {

    private static final String CLIENT_IP = "0.0.0.0";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Test
    public void loadUserByUsernameTest() {
        User user = (User) customUserDetailsService.loadUserByUsername("test");

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("test");
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
            .hasMessage("Too many login attempts");
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
