package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import biz.oneilenterprise.website.entity.Album;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class AlbumRepositoryTest extends BaseRepository {

    @Autowired
    private AlbumRepository albumRepository;

    private static final String ALBUM_ID = "albumID";
    private static final String ALBUM_NAME = "albumName";
    private static final String ALBUM_CREATOR = "albumCreator";

    @Test
    public void getByIDTest() {
        Optional<Album> album = albumRepository.getFirstById(ALBUM_ID);

        assertTrue(album.isPresent());
        assertThat(album.get().getCreator()).isEqualTo(ALBUM_CREATOR);
        assertThat(album.get().getName()).isEqualTo(ALBUM_NAME);
        assertThat(album.get().getMedias().isEmpty()).isEqualTo(false);
    }

    @Test
    public void getByIDNotFoundTest() {
        Optional<Album> album = albumRepository.getFirstById("ALBUM_ID");

        assertFalse(album.isPresent());
    }

    @Test
    public void getAllByCreatorTest() {
        List<Album> albums = albumRepository.getAllByCreator(ALBUM_CREATOR);

        assertThat(albums.size()).isEqualTo(1);
    }

    @Test
    public void getAllByCreatorNotFoundTest() {
        List<Album> albums = albumRepository.getAllByCreator("ALBUM_CREATOR");

        assertThat(albums.size()).isEqualTo(0);
    }

    @Test
    public void getTotalAlbumsByUserTest() {
        long count = albumRepository.getTotalAlbumsByUser(ALBUM_CREATOR);

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void getTotalAlbumsByUserNotFoundTest() {
        long count = albumRepository.getTotalAlbumsByUser("ALBUM_CREATOR");

        assertThat(count).isEqualTo(0);
    }
}
