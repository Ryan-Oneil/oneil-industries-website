package biz.oneilindustries.website.repository;

import biz.oneilindustries.website.entity.Album;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album, String> {

    Optional<Album> getFirstByName(String albumName);
}
