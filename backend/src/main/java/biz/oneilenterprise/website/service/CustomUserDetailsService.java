package biz.oneilenterprise.website.service;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.exception.TooManyLoginAttempts;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final LoginAttemptService loginAttemptService;

    public CustomUserDetailsService(UserService userService, LoginAttemptService loginAttemptService) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String ip = getClientIP();

        if (loginAttemptService.isBlocked(ip)) {
            throw new TooManyLoginAttempts("Too many login attempts");
        }
        User user = userService.getUserByEmail(email.toLowerCase());

        if(user == null) {
            throw new UsernameNotFoundException("Invalid email/password");
        }
        return user;
    }

    public String getClientIP() {
        RequestAttributes attrib = RequestContextHolder.getRequestAttributes();

        if (attrib != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attrib).getRequest();
            String xfHeader = request.getHeader("X-Forwarded-For");

            if (xfHeader == null){
                return request.getRemoteAddr();
            }
            return  xfHeader.split(",")[0];
        }
        return "";
    }
}
