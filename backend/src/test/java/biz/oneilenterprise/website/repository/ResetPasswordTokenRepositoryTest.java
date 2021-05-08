package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.PasswordResetToken;
import biz.oneilenterprise.website.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ResetPasswordTokenRepositoryTest extends BaseRepository {

    @Autowired
    private ResetPasswordTokenRepository repository;

    private User user;

    @BeforeEach
    public void setupDatabase() {
        user = new User("UnitTest", "test");
        PasswordResetToken token = new PasswordResetToken("wadegf23", user);

        entityManager.persist(user);
        entityManager.persist(token);
    }

    @Test
    public void getByTokenTest() {
        Optional<PasswordResetToken> token = repository.getByToken("wadegf23");

        assertThat(token.isPresent()).isTrue();
        assertThat(token.get().getToken()).isEqualTo("wadegf23");
    }

    @Test
    public void getByUsernameTest() {
        Optional<PasswordResetToken> token = repository.getByUsername(user);

        assertThat(token.isPresent()).isTrue();
        assertThat(token.get().getUsername().getUsername()).isEqualTo(user.getUsername());
    }
}
