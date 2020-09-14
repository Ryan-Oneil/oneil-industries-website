package biz.oneilindustries.website.aspects;

import static biz.oneilindustries.AppConfig.ADMIN_ROLES;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.exception.NotAuthorisedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserAuthAspect {

    @Pointcut("execution(* biz.oneilindustries.website.controller.UserController.get*(..))")
    private void getUserMethods() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.UserController.update*(..))")
    private void updateUserMethods() {}

    @Pointcut("execution(* biz.oneilindustries.website.controller.UserController.generateAPIJWT(..))")
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
