package biz.oneilenterprise.website.security;

import static biz.oneilenterprise.website.security.SecurityConstants.EXPIRATION_TIME;
import static biz.oneilenterprise.website.security.SecurityConstants.HEADER_STRING;
import static biz.oneilenterprise.website.security.SecurityConstants.REFRESH_TOKEN_PREFIX;
import static biz.oneilenterprise.website.security.SecurityConstants.SECRET;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.exception.LoginException;
import biz.oneilenterprise.website.service.CustomUserDetailsService;
import biz.oneilenterprise.website.service.LoginAttemptService;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final LoginAttemptService loginAttemptService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
        CustomUserDetailsService customUserDetailsService, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
        HttpServletResponse res) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                .readValue(req.getInputStream(), User.class);

            //Exposes the JWT Bearer token to the front-end
            res.setHeader("Access-Control-Expose-Headers", "Authorization");

            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.getEmail(),
                    creds.getPassword(),
                    new ArrayList<>())
            );
        } catch (IOException | InternalAuthenticationServiceException e) {
            try {
                unsuccessfulAuthentication(req, res, new LoginException(e.getMessage()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
        loginAttemptService.loginSucceeded(customUserDetailsService.getClientIP());

        String token = JWT.create()
            .withSubject("refreshToken")
            .withClaim("user", ((User) auth.getPrincipal()).getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, REFRESH_TOKEN_PREFIX + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
        throws IOException {
        loginAttemptService.loginFailed(customUserDetailsService.getClientIP());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().print(failed.getMessage());
        response.getWriter().flush();
    }
}