package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.entity.VerificationToken;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VerificationTokenRepositoryTest extends BaseRepository {

    @Autowired
    private VerificationTokenRepository repository;

    private final User user = new User(69, "test", "test@example.com");

    @Test
    public void findByTokenTest() {
        Optional<VerificationToken> token = repository.findByToken("4f8fe42f-255f-471d-9e93-b8a06d1b0c2f");

        assertThat(token.isPresent()).isTrue();
        assertThat(token.get().getToken()).isEqualTo("4f8fe42f-255f-471d-9e93-b8a06d1b0c2f");
    }

    @Test
    public void findByUsernameTest() {
        Optional<VerificationToken> token = repository.findByUser(user);

        assertThat(token.isPresent()).isTrue();
        assertThat(token.get().getUser().getUsername()).isEqualTo(user.getUsername());
    }
}
