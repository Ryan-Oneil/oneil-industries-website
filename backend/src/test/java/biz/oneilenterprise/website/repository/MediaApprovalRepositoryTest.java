package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import biz.oneilenterprise.website.entity.PublicMediaApproval;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MediaApprovalRepositoryTest extends BaseRepository {

    @Autowired
    private MediaApprovalRepository mediaApprovalRepository;

    private static final int MEDIA_ID = 12;
    private static final String PUBLIC_NAME = "TAMdr530hnUgNCRd.png";
    private static final String STATUS_PENDING = "pending";

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

        assertThat(publicMediaApprovals.size()).isEqualTo(4);
    }

}
