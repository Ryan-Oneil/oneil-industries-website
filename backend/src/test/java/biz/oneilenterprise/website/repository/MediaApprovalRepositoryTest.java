package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import biz.oneilenterprise.website.entity.Media;
import biz.oneilenterprise.website.entity.PublicMediaApproval;
import biz.oneilenterprise.website.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MediaApprovalRepositoryTest extends BaseRepository {

    @Autowired
    private MediaApprovalRepository mediaApprovalRepository;

    private static int MEDIA_ID;
    private static final String PUBLIC_NAME = "public";
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_DENIED = "denied";

    @BeforeEach
    public void setupDatabase() {
        User user = new User("user", "test");
        Media media = new Media("test", "test", "test", user, "test", 5L);
        Media media2 = new Media("test", "test", "test", user, "test", 5L);

        PublicMediaApproval publicMediaApproval = new PublicMediaApproval(media, PUBLIC_NAME, STATUS_PENDING);
        PublicMediaApproval publicMediaApproval2 = new PublicMediaApproval(media2, PUBLIC_NAME, STATUS_DENIED);

        entityManager.persist(user);
        entityManager.persist(media);
        entityManager.persist(media2);
        MEDIA_ID = media.getId();

        entityManager.persist(publicMediaApproval);
        entityManager.persist(publicMediaApproval2);
    }

    @Test
    public void getFirstByMedia_IdTest() {
        Optional<PublicMediaApproval> publicMediaApprovalOptional = mediaApprovalRepository.getFirstByMedia_Id(MEDIA_ID);

        assertTrue(publicMediaApprovalOptional.isPresent());
        assertThat(publicMediaApprovalOptional.get().getPublicName()).isEqualTo(PUBLIC_NAME);
        assertThat(publicMediaApprovalOptional.get().getStatus()).isEqualTo(STATUS_PENDING);
        assertThat(publicMediaApprovalOptional.get().getMedia()).isNotNull();
    }

    @Test
    public void findAllByStatusTest() {
        List<PublicMediaApproval> publicMediaApprovals = mediaApprovalRepository.findAllByStatus(STATUS_PENDING);

        assertThat(publicMediaApprovals.size()).isEqualTo(1);
    }

}
