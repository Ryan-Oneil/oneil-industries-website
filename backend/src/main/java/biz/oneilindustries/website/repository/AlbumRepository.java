package biz.oneilindustries.website.repository;

import biz.oneilindustries.website.entity.Album;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album, String> {

    Optional<Album> getFirstByName(String albumName);
    @EntityGraph(attributePaths  = {"medias"})
    Optional<Album> getFirstById(String id);

    @EntityGraph(attributePaths  = {"medias"})
    List<Album> getAllByCreator(String user);

    @Query("select count(a) from Album a where a.creator = ?1")
    long getTotalAlbumsByUser(String username);
}
