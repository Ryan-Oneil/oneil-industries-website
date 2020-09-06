package biz.oneilindustries.website.repository;

import biz.oneilindustries.website.entity.Media;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends CrudRepository<Media, Integer> {
    List<Media> getAllByOrderByDateAddedDesc(Pageable page);
    List<Media> getAllByUploader(String uploader);
    List<Media> getAllByLinkStatus(String linkStatus);
    Optional<Media> getFirstByFileName(String filename);

    @Query("select case when count(m)> 0 then true else false end from Media m where m.fileName like ?1")
    boolean isFileNameTaken(String fileName);
}
