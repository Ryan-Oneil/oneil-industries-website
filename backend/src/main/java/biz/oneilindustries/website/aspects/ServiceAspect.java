package biz.oneilindustries.website.aspects;

import biz.oneilindustries.website.entity.DiscordUser;
import biz.oneilindustries.website.entity.TeamspeakUser;
import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.service.ManagerService;
import biz.oneilindustries.website.service.UserService;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class ServiceAspect {

    private static final String DEFAULT_ROLE = "member";

    private final ManagerService managerService;

    private final UserService userService;

    @Autowired
    public ServiceAspect(@Lazy ManagerService managerService, @Lazy UserService userService) {
        this.managerService = managerService;
        this.userService = userService;
    }

    @Before("execution(* biz.oneilindustries.website.service.UserService.saveUserDiscordProfile(..))")
    private void updateDiscord(JoinPoint joinPoint) {

        DiscordUser user = (DiscordUser) joinPoint.getArgs()[0];

        if (user.getActivated() == 1) {
            managerService.addDiscordRole(user.getUuid(),DEFAULT_ROLE);
        }
    }

    @Before("execution(* biz.oneilindustries.website.service.UserService.deleteDiscordUUID(..))")
    private void deleteDiscord(JoinPoint joinPoint) {

        DiscordUser user = (DiscordUser) joinPoint.getArgs()[0];

        deleteDiscordRoles(user);
    }

    @Before("execution(* biz.oneilindustries.website.service.UserService.saveUserTeamspeakProfile(..))")
    private void updateTeamspeak(JoinPoint joinPoint) {

        TeamspeakUser teamspeakUser = (TeamspeakUser) joinPoint.getArgs()[0];

        if (teamspeakUser.getActivated() == 1) {
            managerService.addTeamspeakRole(teamspeakUser.getUuid(), DEFAULT_ROLE);
        }
    }

    @Before("execution(* biz.oneilindustries.website.service.UserService.deleteTeamspeakUUID(..))")
    private void deleteTeamspeak(JoinPoint joinPoint) {

        TeamspeakUser teamspeakUser = (TeamspeakUser) joinPoint.getArgs()[0];

        deleteTeamspeakRoles(teamspeakUser);
    }

    @Before("execution(* biz.oneilindustries.website.service.UserService.saveUser(..))")
    public void userUpdate(JoinPoint joinPoint) {

        User user = (User) joinPoint.getArgs()[0];

        if (!user.isEnabled()) {

            List<DiscordUser> discordUsers = userService.getUserDiscordProfiles(user.getUsername());
            List<TeamspeakUser> teamspeakUsers = userService.getUserTeamspeakProfile(user.getUsername());

            if (discordUsers != null) {
                for (DiscordUser discordUser : discordUsers) {
                    deleteDiscordRoles(discordUser);
                }
            }

            if (teamspeakUsers != null) {
                for (TeamspeakUser teamspeakUser : teamspeakUsers) {
                    deleteTeamspeakRoles(teamspeakUser);
                }
            }
        }
    }

    private void deleteDiscordRoles(DiscordUser user) {
        if (user.getActivated() == 1) {
            managerService.deleteDiscordRoles(user.getUuid());
        }
    }

    private void deleteTeamspeakRoles(TeamspeakUser teamspeakUser) {
        if (teamspeakUser.getActivated() == 1) {
            managerService.deleteTeamspeakRoles(teamspeakUser.getUuid());
        }
    }
}
