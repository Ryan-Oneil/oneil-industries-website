package biz.oneilindustries.website.controller;

import static biz.oneilindustries.website.security.SecurityConstants.REFRESH_TOKEN_PREFIX;
import static biz.oneilindustries.website.security.SecurityConstants.SECRET;
import static biz.oneilindustries.website.security.SecurityConstants.SHORT_EXPIRATION_TIME;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
public class TokenRefreshController {

    private final UserService userService;

    @Autowired
    public TokenRefreshController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestBody String refreshToken) {
        // parse the token.
        DecodedJWT decodedToken = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
            .build()
            .verify(refreshToken.replace(REFRESH_TOKEN_PREFIX, ""));

        if (!decodedToken.getSubject().equalsIgnoreCase("refreshToken")) {
            throw new TokenException("Not a valid refresh Token");
        }

        if (decodedToken.getExpiresAt().before(new Date())) {
            throw new TokenException("Refresh token is expired");
        }
        User user = userService.getUser(decodedToken.getClaim("user").asString());

        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return "Bearer " + JWT.create()
            .withSubject("authToken")
            .withClaim("user", user.getUsername())
            .withClaim("role", user.getAuthorities().get(0).getAuthority())
            .withClaim("enabled", user.getEnabled())
            .withExpiresAt(new Date(System.currentTimeMillis() + SHORT_EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));
    }
}
