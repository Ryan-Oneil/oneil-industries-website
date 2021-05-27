package biz.oneilenterprise.website.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import biz.oneilenterprise.website.dto.QuotaDTO;
import biz.oneilenterprise.website.dto.RegisterUserDTO;
import biz.oneilenterprise.website.dto.ShareXConfigDTO;
import biz.oneilenterprise.website.dto.UserDTO;
import biz.oneilenterprise.website.entity.ApiToken;
import biz.oneilenterprise.website.entity.PasswordResetToken;
import biz.oneilenterprise.website.entity.Quota;
import biz.oneilenterprise.website.entity.Role;
import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.entity.VerificationToken;
import biz.oneilenterprise.website.exception.TokenException;
import biz.oneilenterprise.website.exception.UserException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUsersTest() {
        List<UserDTO> users = userService.getUsers();

        assertThat(users).isNotEmpty();
        assertThat(users).size().isEqualTo(15);
    }

    @Test
    public void getRecentUsersTest() {
        List<UserDTO> users = userService.getRecentUsers();

        assertThat(users).isNotEmpty();
        assertThat(users).size().isEqualTo(5);
    }

    @Test
    public void getUserTest() {
        User user = userService.getUser("user1");

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("user1");
    }

    @Test
    public void getUserNotExists() {
        assertThatThrownBy(() -> userService.getUser("notfound"))
            .isExactlyInstanceOf(UsernameNotFoundException.class)
            .hasMessage("notfound doesn't exist");
    }

    @Test
    public void getUserByEmailTest() {
        User user = userService.getUserByEmail("test@example.com");

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void getUserByEmailNotExists() {
        assertThatThrownBy(() -> userService.getUser("notfound"))
            .isExactlyInstanceOf(UsernameNotFoundException.class)
            .hasMessage("notfound doesn't exist");
    }

    @Test
    public void registerUserTest() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO("integration_test", "test", "integration_test@oneilenterprise.biz");
        userService.registerUser(registerUserDTO);

        User user = userService.getUser(registerUserDTO.getUsername());

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(registerUserDTO.getEmail());
        assertThat(user.getUsername()).isEqualTo(registerUserDTO.getUsername());
        assertThat(user.getEnabled()).isFalse();
        assertThat(user.getQuota()).isNotNull();
        assertThat(user.getRole()).isEqualTo("ROLE_UNREGISTERED");
    }

    @Test
    public void registerUserTakenTest() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO("user1", "test", "integration_test@oneilenterprise.biz");

        assertThatThrownBy(() -> userService.registerUser(registerUserDTO))
            .isExactlyInstanceOf(UserException.class)
            .hasMessage("Username is taken");
    }

    @Test
    public void registerUserEmailTakenTest() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO("integration_test", "test", "test@example.com");

        assertThatThrownBy(() -> userService.registerUser(registerUserDTO))
            .isExactlyInstanceOf(UserException.class)
            .hasMessage("Email is taken");
    }

    @Test
    public void validateUsernameTest() {
        assertDoesNotThrow(() ->  userService.validateUsername("notTaken"));
    }

    @Test
    public void validateUsernameTakenTest() {
        assertThatThrownBy(() -> userService.validateUsername("user1"))
            .isExactlyInstanceOf(UserException.class)
            .hasMessage("Username is taken");
    }

    @Test
    public void validateEmailTest() {
        assertDoesNotThrow(() ->  userService.validateEmail("notTaken"));
    }

    @Test
    public void validateEmailTakenTest() {
        assertThatThrownBy(() -> userService.validateEmail("test@example.com"))
            .isExactlyInstanceOf(UserException.class)
            .hasMessage("Email is taken");
    }

    @Test
    public void updateUserTest() {
        UserDTO userDTO = new UserDTO(1, "newName", "newEmail@oneilenterprise.biz", "ROLE_NEW", true);

        userService.updateUser(userDTO, "user1");

        User user = userService.getUser("user1");

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(user.getRole()).isEqualTo(userDTO.getRole());
    }

    @Test
    public void updateUserNotExistsTest() {
        assertThatThrownBy(() -> userService.updateUser(new UserDTO(), "notfound"))
            .isExactlyInstanceOf(UsernameNotFoundException.class)
            .hasMessage("notfound doesn't exist");
    }

    @Test
    public void createVerificationTokenTest() {
        User user = userService.getUser("user1");
        VerificationToken token = userService.createVerificationToken(user);

        VerificationToken verificationToken = userService.getVerificationToken(token.getToken());

        assertThat(verificationToken).isNotNull();
        assertThat(verificationToken.getUser()).isNotNull();
    }

    @Test
    public void getVerificationTokenTest() {
        VerificationToken token = userService.getVerificationToken("e1d59296-5167-4986-897a-324c170f6e0f");

        assertThat(token).isNotNull();
        assertThat(token.getUser()).isNotNull();
    }

    @Test
    public void getVerificationTokenNotExistsTest() {
        assertThatThrownBy(() -> userService.getVerificationToken("22"))
            .isExactlyInstanceOf(TokenException.class)
            .hasMessage("Invalid Verification Token Link");
    }

    @Test
    public void deleteVerificationTokenTest() {
        VerificationToken token = userService.getVerificationToken("e1d59296-5167-4986-897a-324c170f6e0f");

        userService.deleteVerificationToken(token);

        assertThatThrownBy(() -> userService.getVerificationToken(token.getToken()))
            .isExactlyInstanceOf(TokenException.class)
            .hasMessage("Invalid Verification Token Link");
    }

    @Test
    public void generateResetTokenTest() {
        PasswordResetToken token = userService.generateResetToken("test@example.com");

        assertThat(token).isNotNull();
    }

    @Test
    public void getResetTokenTest() {
        PasswordResetToken token = userService.getResetToken("19fcfd72-5532-44bb-9b06-90351cc6ec6d");

        assertThat(token).isNotNull();
    }

    @Test
    public void getResetTokenNotExistsTest() {
        assertThatThrownBy(() -> userService.getResetToken("notFound"))
            .isExactlyInstanceOf(TokenException.class)
            .hasMessage("Invalid Reset token");
    }

    @Test
    public void updateUserQuotaTest() {
        QuotaDTO quotaDTO = new QuotaDTO(50, 50, true);

        userService.updateUserQuota(quotaDTO, "test");

        User user = userService.getUser("test");
        Quota quota = user.getQuota();

        assertThat(quota).isNotNull();
        assertThat(quota.getMax()).isEqualTo(quotaDTO.getMax());
        assertThat(quota.isIgnoreQuota()).isEqualTo(quotaDTO.isIgnoreQuota());
    }

    @Test
    public void changeQuotaUsedAmountAddTest() {
        userService.changeQuotaUsedAmount("test", 150);

        User user = userService.getUser("test");

        assertThat(user.getQuota().getUsed()).isEqualTo(150L);
    }

    @Test
    public void changeQuotaUsedAmountMinusTest() {
        userService.changeQuotaUsedAmount("test", 150);
        userService.changeQuotaUsedAmount("test", -50);

        User user = userService.getUser("test");

        assertThat(user.getQuota().getUsed()).isEqualTo(100L);
    }

    @Test
    public void getRemainingQuotaTest() {
        long remainingQuota = userService.getRemainingQuota("test");

        assertThat(remainingQuota).isEqualTo(FileUtils.ONE_GB);
    }

    @Test
    public void getRemainingQuotaIgnoreTest() {
        long remainingQuota = userService.getRemainingQuota("albumCreator");

        assertThat(remainingQuota).isEqualTo(-1L);
    }

    @Test
    public void getApiTokenByUserTest() {
        User user = new User();
        user.setId(69);

        ApiToken apiToken = userService.getApiTokenByUser(user);

        assertThat(apiToken).isNotNull();
    }

    @Test
    public void generateApiTokenTest() {
        User user = new User();
        user.setUsername("test");

        ApiToken apiToken = userService.generateApiToken(user);

        assertThat(apiToken).isNotNull();
        assertThat(apiToken.getToken()).isNotEmpty();
        assertThat(apiToken.getUser()).isNotNull();
        assertThat(apiToken.getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void generateShareXAPIFileTest() {
        User user = new User();
        user.setId(69);

        ShareXConfigDTO shareXConfigDTO = userService.generateShareXAPIFile(user);

        assertThat(shareXConfigDTO).isNotNull();
    }

    @Test
    public void confirmUserRegistrationTest() {
        userService.confirmUserRegistration("e1d59296-5167-4986-897a-324c170f6e0f");
        User user = userService.getUser("test2");

        assertThat(user.isEnabled()).isTrue();
        assertThatThrownBy(() -> userService.getVerificationToken("e1d59296-5167-4986-897a-324c170f6e0f"))
            .isExactlyInstanceOf(TokenException.class)
            .hasMessage("Invalid Verification Token Link");
    }

    @Test
    public void confirmUserRegistrationExpiredTest() {
        String message = userService.confirmUserRegistration("4f8fe42f-255f-471d-9e93-b8a06d1b0c2f");

        assertThat(message).isEqualTo("This link has expired, a new one has been emailed");
    }

    @Test
    public void getUserStatsTest() {
        UserDTO userDTO = userService.getUserStats("test");

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getQuota()).isNotNull();
    }

    @Test
    public void getTotalUsedQuotaTest() {
        long totalQuota = userService.getTotalUsedQuota();

        assertThat(totalQuota).isEqualTo(2500L);
    }

    @Test
    public void getRolesTest() {
        List<Role> roles = userService.getRoles();

        assertThat(roles).isNotEmpty();
    }

    @Test
    public void changeUserAccountStatusTest() {
        userService.changeUserAccountStatus(false, "user1");

        User user = userService.getUser("user1");

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void quotaToDTOTest() {
        Quota quota = new Quota(new User(),50, 50, true);

        QuotaDTO quotaDTO = userService.quotaToDTO(quota);

        assertThat(quotaDTO).isNotNull();
        assertThat(quotaDTO.getMax()).isEqualTo(quota.getMax());
        assertThat(quotaDTO.getUsed()).isEqualTo(quota.getUsed());
        assertThat(quotaDTO.isIgnoreQuota()).isEqualTo(quota.isIgnoreQuota());
    }

    @Test
    public void usersToDTOsTest() {
        List<UserDTO> userDTOS = userService.usersToDTOs(Collections.singletonList(new User()));

        assertThat(userDTOS).isNotEmpty();
    }
}
