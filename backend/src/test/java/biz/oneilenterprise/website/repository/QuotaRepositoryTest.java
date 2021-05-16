package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QuotaRepositoryTest extends BaseRepository {

    @Autowired
    private QuotaRepository repository;

    @Test
    public void getTotalUsedTest() {
        long totalUsed = repository.getTotalUsed();

        assertThat(totalUsed).isEqualTo(2500);
    }
}
