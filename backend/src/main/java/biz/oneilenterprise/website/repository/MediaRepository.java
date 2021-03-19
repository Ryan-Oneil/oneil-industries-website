package biz.oneilenterprise.website.repository;

import biz.oneilenterprise.website.entity.Album;
import biz.oneilenterprise.website.entity.Media;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends CrudRepository<Media, Integer> {
    List<Media> getAllByMediaTypeOrderByIdDesc(String mediaType, Pageable page);
    List<Media> getAllByLinkStatusAndMediaTypeOrderByIdDesc(String linkStatus, String mediaType, Pageable page);
    Optional<Media> getFirstByFileName(String filename);
    List<Media> getAllByUploaderAndMediaTypeOrderByIdDesc(String uploader, String mediaType, Pageable page);
    List<Media> findTop5ByUploaderOrderByIdDesc(String uploader);

    @Query("select count(m) from Media m where m.uploader = ?1 and m.mediaType = ?2")
    long getTotalMediasByUserAndType(String username, String mediaType);

    @Query("select count(m) from Media m where m.linkStatus = ?1 and m.mediaType = ?2")
    long getTotalMediaByStatusAndMediaType(String status, String mediaType);

    @Query("select count(m) from Media m where m.mediaType = ?1")
    long getTotalMediaByType(String mediaType);

    @Query("select count(m) from Media m where m.uploader = ?1")
    long getTotalByUser(String username);

    @Query("select sum(m.size) from Media m where m.id in ?1")
    Long getTotalMediasSize(Integer[] mediaIds);

    @Query("select m from Media m where m.id in ?1")
    List<Media> getAllByIds(Integer[] mediaIds);

    @Transactional
    @Modifying
    @Query("update Media m set m.album = ?1 where m.id in ?2 and m.uploader = ?3")
    void setMediasAlbum(Album album, int[] mediaIds, String uploader);

    @Transactional
    @Modifying
    @Query("delete from Media m where m.id in ?1")
    void deleteMediasByIds(Integer[] mediaIds);
}
