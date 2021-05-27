package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.PasswordResetToken;
import biz.oneilenterprise.website.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ResetPasswordTokenRepositoryTest extends BaseRepository {

    @Autowired
    private ResetPasswordTokenRepository repository;

    private final User user = new User(70,"test2", "ROLE_UNREGISTERED");

    @Test
    public void getByTokenTest() {
        Optional<PasswordResetToken> token = repository.getByToken("19fcfd72-5532-44bb-9b06-90351cc6ec6d");

        assertThat(token.isPresent()).isTrue();
        assertThat(token.get().getToken()).isEqualTo("19fcfd72-5532-44bb-9b06-90351cc6ec6d");
    }

    @Test
    public void getByUsernameTest() {
        Optional<PasswordResetToken> token = repository.getByUser(user);

        assertThat(token.isPresent()).isTrue();
        assertThat(token.get().getUser().getUsername()).isEqualTo(user.getUsername());
    }
}
