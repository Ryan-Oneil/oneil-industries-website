package biz.oneilenterprise.website.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.oneilenterprise.website.entity.Album;
import biz.oneilenterprise.website.entity.Media;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class MediaRepositoryTest extends BaseRepository {

    @Autowired
    private MediaRepository mediaRepository;

    private static final int AMOUNT_OF_MEDIAS = 15;
    private static final int AMOUNT_OF_MEDIAS_PER_USER = 5;
    private static final String PUBLIC = "public";
    private static final String UNLISTED = "unlisted";
    private static final String USERNAME1 = "user1";
    private static final String USERNAME2 = "user2";
    private static final Pageable page = PageRequest.of(0, AMOUNT_OF_MEDIAS_PER_USER);
    private final Integer[] mediaIds = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private final Album album = new Album("test", "test", USERNAME1);

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
        List<Media> medias = mediaRepository.getAllByLinkStatusOrderByIdDesc(UNLISTED, page);

        assertThat(medias.size()).isEqualTo(AMOUNT_OF_MEDIAS_PER_USER);
        medias.forEach(media -> assertThat(media.getLinkStatus()).isEqualTo(UNLISTED));
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
        assertThat(mediasSize).isEqualTo(2400093L);
    }

    @Test
    public void getAllByIdsTest() {
        List<Media> medias = mediaRepository.getAllByIds(mediaIds);

        assertThat(medias.size()).isEqualTo(mediaIds.length);
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
