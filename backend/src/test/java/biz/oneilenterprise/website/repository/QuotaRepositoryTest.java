package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.Quota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QuotaRepositoryTest extends BaseRepository {

    @Autowired
    private QuotaRepository repository;

    @BeforeEach
    public void setupDatabase() {
        Quota quota = new Quota("test", 2500, 24, false);
        Quota quota2 = new Quota("test2", 36, 24, false);

        entityManager.persist(quota2);
        entityManager.persist(quota);
    }

    @Test
    public void getTotalUsedTest() {
        long totalUsed = repository.getTotalUsed();

        assertThat(totalUsed).isEqualTo(2536);
    }

    @Test
    public void getFirstByUsernameTest() {
        Quota quota = repository.getFirstByUsername("test");

        assertThat(quota).isNotNull();
    }
}
