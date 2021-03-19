package biz.oneilenterprise.website.aspects;

import static biz.oneilenterprise.AppConfig.ADMIN_ROLES;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.exception.NotAuthorisedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserAuthAspect {

    @Pointcut("execution(* biz.oneilenterprise.website.controller.UserController.get*(..))")
    private void getUserMethods() {}

    @Pointcut("execution(* biz.oneilenterprise.website.controller.UserController.update*(..))")
    private void updateUserMethods() {}

    @Pointcut("execution(* biz.oneilenterprise.website.controller.UserController.generateAPIJWT(..))")
    private void generateJWT() {}

    @Before("getUserMethods() || updateUserMethods() || generateJWT()")
    public void checkPermission(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        String username = (String) args[0];
        User user = (User) ((Authentication) args[1]).getPrincipal();

        if (!username.equalsIgnoreCase(user.getUsername()) && !ADMIN_ROLES.contains(user.getRole())) {
            throw new NotAuthorisedException("Not Authorised to access this endpoint");
        }
    }
}
