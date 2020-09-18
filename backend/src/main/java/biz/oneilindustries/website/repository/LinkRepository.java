package biz.oneilindustries.website.repository;

import biz.oneilindustries.website.entity.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  LinkRepository extends PagingAndSortingRepository<Link, String> {
    Optional<Link> findById(String id);

    @Query("select l from Link l where l.creator.username = ?1 ")
    List<Link> getAllByCreator(String username);

    @EntityGraph(attributePaths  = {"files"})
    Optional<Link> getById(String id);

    @Query("select l from Link l where l.creator.username = ?1 and l.expiryDatetime < CURRENT_TIMESTAMP ")
    List<Link> getAllExpiredByCreator(String username);

    @Query("select l from Link l where l.creator.username = ?1 and l.expiryDatetime > CURRENT_TIMESTAMP ")
    List<Link> getAllActiveByCreator(String username);

    List<Link> findTop5ByCreator_UsernameOrderByCreationDateDesc(String username);

    @Query("select count(l) from Link l where l.creator.username = ?1")
    Integer getUserLinkCount(String username);

    @Query("select COALESCE(sum (l.views), 0) from Link l where l.creator.username = ?1")
    Long getUserTotalViews(String username);

    List<Link> findTop5ByCreator_UsernameOrderByViewsDesc(String username);

    @Query("select COALESCE(sum(l.views), 0) from Link l")
    long getTotalViews();

    @Query("select count (l) from Link l")
    Long getTotalLinks();

    List<Link> getAllByCreator_Username(String username, Pageable pageable);
    List<Link> getTop5ByCreator_UsernameOrderByCreationDateDesc(String username);
    List<Link> getTop5ByCreator_UsernameOrderByViewsDesc(String username);
    List<Link> findTop5ByOrderByIdDesc();
    List<Link> findTop5ByOrderByViewsDesc();
}
