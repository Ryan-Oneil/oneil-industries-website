package biz.oneilenterprise.website.repository;

import biz.oneilenterprise.website.entity.PublicMediaApproval;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaApprovalRepository extends CrudRepository<PublicMediaApproval, Integer> {
    Optional<PublicMediaApproval> getFirstByMedia_Id(int id);
    List<PublicMediaApproval> findAllByStatus(String status);
}
