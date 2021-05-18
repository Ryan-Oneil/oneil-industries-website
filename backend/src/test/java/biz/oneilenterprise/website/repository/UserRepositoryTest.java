package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends BaseRepository {

    public static final String EMAIL = "test@example.com";

    @Autowired
    private UserRepository repository;

    @Test
    public void getByUsernameTest() {
        Optional<User> user = repository.getByUsername("test");

        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUsername()).isEqualTo("test");
    }

    @Test
    public void getUsersByEmailTest() {
        Optional<User> user = repository.getUsersByEmail(EMAIL);

        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    public void getAllUsersTest() {
        List<User> users = repository.findAllByOrderByIdDesc();

        assertThat(users.size()).isEqualTo(15);
    }

    @Test
    public void isUsernameTakenTest() {
        boolean usernameTaken = repository.isUsernameTaken("test");

        assertThat(usernameTaken).isTrue();
    }

    @Test
    public void isUsernameNotTakenTest() {
        boolean usernameTaken = repository.isUsernameTaken("free");

        assertThat(usernameTaken).isFalse();
    }

    @Test
    public void isEmailTakenTest() {
        boolean usernameTaken = repository.isEmailTaken(EMAIL);

        assertThat(usernameTaken).isTrue();
    }

    @Test
    public void isEmailNotTakenTest() {
        boolean usernameTaken = repository.isEmailTaken("test");

        assertThat(usernameTaken).isFalse();
    }
}
