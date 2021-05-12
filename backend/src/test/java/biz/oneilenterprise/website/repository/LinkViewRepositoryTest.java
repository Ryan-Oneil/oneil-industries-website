package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.Link;
import biz.oneilenterprise.website.entity.LinkView;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkViewRepositoryTest extends BaseRepository {

    @Autowired
    private LinkViewRepository repository;

    @Test
    public void getFirstByIpAndLinkTest() {
        Link link = new Link();
        link.setId("Yoy1j229znwU9sfu");

        Optional<LinkView> view = repository.getFirstByIpAndLink("0.0.0.0", link);

        assertThat(view.isPresent()).isTrue();
        assertThat(view.get().getIp()).isEqualTo("0.0.0.0");
    }

    @Test
    public void getFirstByIpAndLinkNotFoundTest() {
        Link link = new Link();
        link.setId("notFound");

        Optional<LinkView> view = repository.getFirstByIpAndLink("0.0.0.0", link);

        assertThat(view.isPresent()).isFalse();
    }
}
