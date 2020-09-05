package biz.oneilindustries.website.repository;

import biz.oneilindustries.website.entity.Quota;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotaRepository extends CrudRepository<Quota, String> {
    @Query("select sum (q.used) from Quota q")
    long getTotalUsed();

    Quota getFirstByUsername(String username);
}
