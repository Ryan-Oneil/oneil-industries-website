package biz.oneilindustries.website.service;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.exception.TooManyLoginAttempts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private final LoginAttemptService loginAttemptService;

    private final HttpServletRequest request;

    @Autowired
    public CustomUserDetailsService(UserService userService, LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String ip = getClientIP();

        if (loginAttemptService.isBlocked(ip)) {
            throw new TooManyLoginAttempts("blocked");
        }

        User user = userService.getUser(username);

        if(user == null) {
            throw new UsernameNotFoundException("Invalid username/password");
        }
        return user;
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
