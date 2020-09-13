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
    List<Media> getAllByUploaderOrderByIdDesc(String uploader, Pageable page);
    List<Media> getAllByLinkStatus(String linkStatus, Pageable page);
    Optional<Media> getFirstByFileName(String filename);
    List<Media> getAllByUploaderAndMediaTypeOrderByIdDesc(String uploader, String mediaType, Pageable page);

    @Query("select case when count(m)> 0 then true else false end from Media m where m.fileName like ?1")
    boolean isFileNameTaken(String fileName);

    @Query("select count(m) from Media m where m.uploader = ?1 and m.mediaType = ?2")
    long getTotalMediasByUserAndType(String username, String mediaType);

    @Query("select count(m) from Media m where m.linkStatus = ?1")
    long getTotalMediaByStatus(String status);
}
