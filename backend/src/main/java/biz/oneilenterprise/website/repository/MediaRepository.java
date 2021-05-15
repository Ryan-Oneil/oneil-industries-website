package biz.oneilenterprise.website.repository;

import biz.oneilenterprise.website.entity.Album;
import biz.oneilenterprise.website.entity.Media;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
    List<Media> getAllByOrderByIdDesc(Pageable page);
    List<Media> getAllByLinkStatusOrderByIdDesc(String linkStatus, Pageable page);
    Optional<Media> getFirstByFileName(String filename);
    List<Media> getAllByUploader_UsernameOrderByIdDesc(String uploader, Pageable page);
    List<Media> findTop5ByUploader_UsernameOrderByIdDesc(String uploader);

    @Query("select count(m) from Media m where m.uploader.username = ?1")
    long getTotalMediasByUser(String username);

    @Query("select count(m) from Media m where m.linkStatus = ?1")
    long getTotalMediaByStatus(String status);

    @Query("select count(m) from Media m where m.uploader.username = ?1")
    long getTotalByUser(String username);

    @Query("select sum(m.size) from Media m where m.id in ?1")
    Long getTotalMediasSize(Integer[] mediaIds);

    @Query("select m from Media m where m.id in ?1")
    List<Media> getAllByIds(Integer[] mediaIds);

    @Transactional
    @Modifying
    @Query("update Media m set m.album = ?1 where m.id in ?2 and m.uploader.username = ?3")
    void setMediasAlbum(Album album, int[] mediaIds, String uploader);

    @Transactional
    @Modifying
    @Query("update Media m set m.linkStatus = ?1 where m.id in ?2 and m.uploader.username = ?3")
    void updateMediaPrivacy(String privacyStatus, Integer[] mediaIds, String uploader);

    @Transactional
    @Modifying
    @Query("delete from Media m where m.id in ?1")
    void deleteMediasByIds(Integer[] mediaIds);
}
