package biz.oneilenterprise.website.eventlisteners;

import biz.oneilenterprise.website.service.CustomUserDetailsService;
import biz.oneilenterprise.website.service.LoginAttemptService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureEventListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;

    private final CustomUserDetailsService customUserDetailsService;

    public AuthenticationFailureEventListener(LoginAttemptService loginAttemptService,
        CustomUserDetailsService customUserDetailsService) {
        this.loginAttemptService = loginAttemptService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        loginAttemptService.loginFailed(customUserDetailsService.getClientIP());
    }
}