package biz.oneilenterprise.website.eventlisteners;

import biz.oneilenterprise.website.service.CustomUserDetailsService;
import biz.oneilenterprise.website.service.LoginAttemptService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    private final CustomUserDetailsService customUserDetailsService;

    private final LoginAttemptService loginAttemptService;

    public AuthenticationSuccessEventListener(CustomUserDetailsService customUserDetailsService,
        LoginAttemptService loginAttemptService) {
        this.customUserDetailsService = customUserDetailsService;
        this.loginAttemptService = loginAttemptService;
    }

    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        loginAttemptService.loginSucceeded(customUserDetailsService.getClientIP());
    }
}