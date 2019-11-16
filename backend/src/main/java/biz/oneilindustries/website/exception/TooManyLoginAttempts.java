package biz.oneilindustries.website.exception;

import org.springframework.security.core.AuthenticationException;

public class TooManyLoginAttempts extends AuthenticationException {

    public TooManyLoginAttempts(String msg) {
        super(msg);
    }
}
