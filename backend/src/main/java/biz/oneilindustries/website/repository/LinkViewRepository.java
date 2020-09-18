package biz.oneilindustries.website.repository;

import biz.oneilindustries.website.entity.Link;
import biz.oneilindustries.website.entity.LinkView;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface LinkViewRepository extends CrudRepository<LinkView, Integer> {
    Optional<LinkView> getFirstByIpAndLink(String ip, Link link);
}
