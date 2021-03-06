package biz.oneilenterprise.website.repository;

import biz.oneilenterprise.website.entity.Link;
import biz.oneilenterprise.website.entity.LinkView;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface LinkViewRepository extends CrudRepository<LinkView, Integer> {
    Optional<LinkView> getFirstByIpAndLink(String ip, Link link);
}
