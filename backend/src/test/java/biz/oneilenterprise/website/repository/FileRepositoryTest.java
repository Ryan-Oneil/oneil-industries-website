package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.Link;
import biz.oneilenterprise.website.entity.SharedFile;
import biz.oneilenterprise.website.entity.User;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FileRepositoryTest extends BaseRepository {

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    public void setupDatabase() {
        User user = new User("UnitTest", "test");
        Link link = new Link("testLink", "test", user, new Date(), new Date(), 230);
        SharedFile file = new SharedFile("awd", "test.png", 230, link);
        SharedFile file2 = new SharedFile("dwa", "test2.png", 450, link);

        entityManager.persist(user);
        entityManager.persist(link);
        entityManager.persist(file);
        entityManager.persist(file2);
    }

    @Test
    public void getByIDTest() {
        Optional<SharedFile> foundFile = fileRepository.getById("awd");

        assertThat(foundFile.isPresent()).isTrue();
        foundFile.ifPresent(sharedFile -> assertThat(sharedFile.getId()).isEqualTo("awd"));
        assertThat(foundFile.get().getLink().getCreator().getUsername().equals("UnitTest"));
    }

    @Test
    public void getTotalFileCountTest() {
        long count = fileRepository.getTotalFiles();

        assertThat(count).isEqualTo(2);
    }

    @Test
    public void getTotalFileCountByUser() {
        long count = fileRepository.getUserFileCount("UnitTest");

        assertThat(count).isEqualTo(2);
    }
}
