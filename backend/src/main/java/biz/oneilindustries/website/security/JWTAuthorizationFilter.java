package biz.oneilindustries.website.security;


import static biz.oneilindustries.website.security.SecurityConstants.HEADER_STRING;
import static biz.oneilindustries.website.security.SecurityConstants.SECRET;
import static biz.oneilindustries.website.security.SecurityConstants.TOKEN_PREFIX;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private UserService userService;

    public JWTAuthorizationFilter(AuthenticationManager authManager, ApplicationContext ctx) {
        super(authManager);
        userService = ctx.getBean(UserService.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            DecodedJWT decodedToken = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));

            if (decodedToken.getExpiresAt().before(new Date())) {
                return null;
            }

            String user = decodedToken.getSubject();

            if (user != null) {
                User userDetails = userService.getUser(user);
                return new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
            }
            return null;
        }
        return null;
    }
}