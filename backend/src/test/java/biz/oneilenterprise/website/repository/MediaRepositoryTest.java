package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.Album;
import biz.oneilenterprise.website.entity.Media;
import biz.oneilenterprise.website.entity.User;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class MediaRepositoryTest extends BaseRepository {

    @Autowired
    private MediaRepository mediaRepository;

    private static final int AMOUNT_OF_MEDIAS = 10;
    private static final int AMOUNT_OF_MEDIAS_PER_USER = 5;
    private static final String PUBLIC = "public";
    private static final String PRIVATE = "private";
    private static final String USERNAME1 = "user1";
    private static final String USERNAME2 = "user2";
    private static final Pageable page = PageRequest.of(0, AMOUNT_OF_MEDIAS_PER_USER);
    private final Integer[] mediaIds = new Integer[AMOUNT_OF_MEDIAS];
    private final Album album = new Album("awd", "awd", USERNAME1);

    @BeforeEach
    public void setupDatabase() {
        User user = new User(USERNAME1, "test");
        User user2 = new User(USERNAME2, "test");

        entityManager.persist(album);
        entityManager.persist(user);
        entityManager.persist(user2);

        for (int i = 0; i < AMOUNT_OF_MEDIAS_PER_USER; i++) {
            Media media = new Media("test", "test", PUBLIC, user, "", 10L);

            entityManager.persist(media);
            mediaIds[i] = media.getId();
        }

        for (int i = 0; i < AMOUNT_OF_MEDIAS_PER_USER; i++) {
            Media media = new Media("test", "test", PRIVATE, user2, "", 10L);

            entityManager.persist(media);
            mediaIds[i + AMOUNT_OF_MEDIAS_PER_USER] = media.getId();
        }
    }

    @Test
    public void get10ByOrderByIdDescTest() {
        List<Media> medias = mediaRepository.getAllByOrderByIdDesc(PageRequest.of(0, AMOUNT_OF_MEDIAS));

        assertThat(medias.size()).isEqualTo(AMOUNT_OF_MEDIAS);
    }

    @Test
    public void get5ByOrderByIdDescTest() {
        List<Media> medias = mediaRepository.getAllByOrderByIdDesc(page);

        assertThat(medias.size()).isEqualTo(AMOUNT_OF_MEDIAS_PER_USER);
    }

    @Test
    public void getAllByLinkStatusPublicOrderByIdDescTest() {
        List<Media> medias = mediaRepository.getAllByLinkStatusOrderByIdDesc(PUBLIC, page);

        assertThat(medias.size()).isEqualTo(AMOUNT_OF_MEDIAS_PER_USER);
        medias.forEach(media -> assertThat(media.getLinkStatus()).isEqualTo(PUBLIC));
    }

    @Test
    public void getAllByLinkStatusPrivateOrderByIdDescTest() {
        List<Media> medias = mediaRepository.getAllByLinkStatusOrderByIdDesc(PRIVATE, page);

        assertThat(medias.size()).isEqualTo(AMOUNT_OF_MEDIAS_PER_USER);
        medias.forEach(media -> assertThat(media.getLinkStatus()).isEqualTo(PRIVATE));
    }

    @Test
    public void getFirstByFileNameTest() {
        Optional<Media> media = mediaRepository.getFirstByFileName("test");

        assertThat(media.isPresent()).isTrue();
        assertThat(media.get().getFileName()).isEqualTo("test");
    }

    @Test
    public void getFirstByFileNameNotfoundTest() {
        Optional<Media> media = mediaRepository.getFirstByFileName("none");

        assertThat(media.isPresent()).isFalse();
    }

    @Test
    public void getAllByUploader_UsernameOrderByIdDescTest() {
        List<Media> medias = mediaRepository.getAllByUploader_UsernameOrderByIdDesc(USERNAME1, page);

        assertThat(medias.size()).isEqualTo(AMOUNT_OF_MEDIAS_PER_USER);
        medias.forEach(media -> assertThat(media.getUploader().getUsername()).isEqualTo(USERNAME1));
    }

    @Test
    public void findTop5ByUploader_UsernameOrderByIdDescTest() {
        List<Media> medias = mediaRepository.findTop5ByUploader_UsernameOrderByIdDesc(USERNAME2);

        assertThat(medias.size()).isEqualTo(AMOUNT_OF_MEDIAS_PER_USER);
        medias.forEach(media -> assertThat(media.getUploader().getUsername()).isEqualTo(USERNAME2));
    }

    @Test
    public void getTotalMediasByUserTest() {
        long mediaCount = mediaRepository.getTotalMediasByUser(USERNAME1);

        assertThat(mediaCount).isEqualTo(AMOUNT_OF_MEDIAS_PER_USER);
    }

    @Test
    public void getTotalMediaByStatusTest() {
        long mediaCount = mediaRepository.getTotalMediaByStatus(PUBLIC);

        assertThat(mediaCount).isEqualTo(5L);
    }

    @Test
    public void getTotalByUserTest() {
        long mediaCount = mediaRepository.getTotalByUser(USERNAME2);

        assertThat(mediaCount).isEqualTo(AMOUNT_OF_MEDIAS_PER_USER);
    }

    @Test
    public void getTotalMediasSizeTest() {
        Long mediasSize = mediaRepository.getTotalMediasSize(mediaIds);

        assertThat(mediasSize).isNotNull();
        assertThat(mediasSize).isEqualTo(10L * AMOUNT_OF_MEDIAS);
    }

    @Test
    public void getAllByIdsTest() {
        List<Media> medias = mediaRepository.getAllByIds(mediaIds);

        assertThat(medias.size()).isEqualTo(AMOUNT_OF_MEDIAS);
    }

    @Test
    public void setMediasAlbumTest() {
        int[] mediasId = Arrays.stream(mediaIds).mapToInt(Integer::intValue).toArray();
        mediaRepository.setMediasAlbum(album, mediasId, USERNAME1);

        refreshEntities();

        List<Media> medias = mediaRepository.getAllByUploader_UsernameOrderByIdDesc(USERNAME1, page);
        medias.forEach(media -> assertThat(media.getAlbum()).isNotNull());
    }

    @Test
    public void updateMediaPrivacyTest() {
        mediaRepository.updateMediaPrivacy(PUBLIC, mediaIds, USERNAME2);

        refreshEntities();

        List<Media> medias = mediaRepository.getAllByUploader_UsernameOrderByIdDesc(USERNAME2, page);
        medias.forEach(media -> assertThat(media.getLinkStatus()).isEqualTo(PUBLIC));
    }

    @Test
    public void deleteMediasByIdsTest() {
        mediaRepository.deleteMediasByIds(mediaIds);

        List<Media> medias = mediaRepository.getAllByIds(mediaIds);
        assertThat(medias.isEmpty()).isTrue();
    }

    public void refreshEntities() {
        List<Media> medias = mediaRepository.getAllByIds(mediaIds);
        medias.forEach(media -> entityManager.refresh(media));
    }
}
