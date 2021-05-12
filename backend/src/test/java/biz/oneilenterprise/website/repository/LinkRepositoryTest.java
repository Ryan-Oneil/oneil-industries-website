package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.Link;
import biz.oneilenterprise.website.entity.SharedFile;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkRepositoryTest extends BaseRepository {

    @Autowired
    private LinkRepository linkRepository;

    @Test
    public void findByIdTest() {
        Optional<Link> link = linkRepository.findById("aa98m1shTbTz7gdf");

        assertThat(link.isPresent()).isTrue();
        assertThat(link.get().getId()).isEqualTo("aa98m1shTbTz7gdf");
    }

    @Test
    public void getAllByCreatorTest() {
        List<Link> links = linkRepository.getAllByCreator("test");

        assertThat(links.size()).isEqualTo(15);
        assertThat(links.get(0).getCreator().getUsername()).isEqualTo("test");
    }

    @Test
    public void getByIdTest() {
        Optional<Link> link = linkRepository.getById("aa98m1shTbTz7gdf");

        assertThat(link.isPresent()).isTrue();
        assertThat(link.get().getId()).isEqualTo("aa98m1shTbTz7gdf");

        List<SharedFile> files = link.get().getFiles();

        assertThat(files.size()).isEqualTo(2);
    }

    @Test
    public void findTop5ByCreator_UsernameOrderByCreationDateDescTest() {
        List<Link> links = linkRepository.findTop5ByCreator_UsernameOrderByCreationDateDesc("test");

        Link link1 = links.get(0);
        Link link2 = links.get(1);

        assertThat(link1.getCreationDate().after(link2.getCreationDate())).isEqualTo(true);
        assertThat(link1.getCreator().getUsername()).isEqualTo("test");
    }

    @Test
    public void getTotalViewsTest() {
        long views = linkRepository.getTotalViews();

        assertThat(views).isEqualTo(4);
    }

    @Test
    public void getTotalLinksTest() {
        long totalLinkCount = linkRepository.getTotalLinks();

        assertThat(totalLinkCount).isEqualTo(15);
    }
}
