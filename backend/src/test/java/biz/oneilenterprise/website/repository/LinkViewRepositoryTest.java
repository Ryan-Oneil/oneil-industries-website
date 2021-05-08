package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.Link;
import biz.oneilenterprise.website.entity.LinkView;
import biz.oneilenterprise.website.entity.User;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkViewRepositoryTest extends BaseRepository {

    @Autowired
    private LinkViewRepository repository;

    private Link link;

    @BeforeEach
    public void setupDatabase() {
        User user = new User("UnitTest", "test");
        link = new Link("testLink", "test", user, new Date(), new Date(), 680);
        link.setViews(1);

        LinkView view = new LinkView("0.0.0.0", link);

        entityManager.persist(user);
        entityManager.persist(link);
        entityManager.persist(view);
    }

    @Test
    public void getFirstByIpAndLink() {
        Optional<LinkView> view = repository.getFirstByIpAndLink("0.0.0.0", link);

        assertThat(view.isPresent()).isTrue();
        assertThat(view.get().getIp()).isEqualTo("0.0.0.0");
    }
}
