package biz.oneilenterprise.website.service;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.exception.TooManyLoginAttempts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String ip = getClientIP();

        if (loginAttemptService.isBlocked(ip)) {
            throw new TooManyLoginAttempts("Too many login attempts");
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
