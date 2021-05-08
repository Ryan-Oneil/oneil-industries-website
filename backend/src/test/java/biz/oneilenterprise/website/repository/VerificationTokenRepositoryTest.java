package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.entity.VerificationToken;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VerificationTokenRepositoryTest extends BaseRepository {

    @Autowired
    private VerificationTokenRepository repository;

    private User user;

    @BeforeEach
    public void setupDatabase() {
        user = new User("UnitTest", "test");
        VerificationToken token = new VerificationToken("awdqwe", user);

        entityManager.persist(user);
        entityManager.persist(token);
    }

    @Test
    public void findByTokenTest() {
        Optional<VerificationToken> token = repository.findByToken("awdqwe");

        assertThat(token.isPresent()).isTrue();
        assertThat(token.get().getToken()).isEqualTo("awdqwe");
    }

    @Test
    public void findByUsernameTest() {
        Optional<VerificationToken> token = repository.findByUsername(user);

        assertThat(token.isPresent()).isTrue();
        assertThat(token.get().getUsername().getUsername()).isEqualTo(user.getUsername());
    }
}
