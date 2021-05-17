package biz.oneilenterprise.website.repository;

import biz.oneilenterprise.website.entity.Quota;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotaRepository extends CrudRepository<Quota, String> {
    @Query("select sum (q.used) from Quota q")
    long getTotalUsed();
}
