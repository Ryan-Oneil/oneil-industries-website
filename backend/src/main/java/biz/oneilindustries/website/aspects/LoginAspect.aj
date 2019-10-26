package biz.oneilindustries.website.aspects;

import biz.oneilindustries.website.entity.PasswordResetToken;
import biz.oneilindustries.website.entity.VerificationToken;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@Aspect
public class LoginAspect {

    @Autowired
    private UserService userService;

    @Pointcut("execution(* biz.oneilindustries.website.controller.LoginController.confirmRegistration(..))")
    private void confirmRegisterToken() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.LoginController.confirmNewPassword(..))")
    private void confirmPasswordToken() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.LoginController.setNewPassword(..))")
    private void setNewPassword() {}

    @Before("confirmRegisterToken()")
    public void beforeToken(JoinPoint joinPoint) {

        String token = (String) joinPoint.getArgs()[0];

        VerificationToken verificationToken = userService.getToken(token);

        performTokenCheck( verificationToken != null);

        checkExpired(verificationToken.getExpiryDate());
    }

    @Before("confirmPasswordToken() || setNewPassword()")
    public void beforePasswordToken(JoinPoint joinPoint) {

        String token = (String) joinPoint.getArgs()[0];

        PasswordResetToken passwordResetToken = userService.getResetToken(token);

        performTokenCheck(passwordResetToken != null);

        checkExpired(passwordResetToken.getExpiryDate());
    }

    public void performTokenCheck(boolean validToken) {

        if (!validToken) {
            throw new TokenException("Invalid token");
        }
    }

    public void checkExpired(Date date) {

        Calendar cal = Calendar.getInstance();
        if ((date.getTime() - cal.getTime().getTime()) <= 0) {
            throw new TokenException("Token has expired");
        }
    }
}
