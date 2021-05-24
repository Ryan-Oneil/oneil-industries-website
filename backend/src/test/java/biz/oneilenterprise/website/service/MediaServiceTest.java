package biz.oneilenterprise.website.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import biz.oneilenterprise.website.dto.AlbumDTO;
import biz.oneilenterprise.website.dto.MediaUploadDTO;
import biz.oneilenterprise.website.dto.MediaDTO;
import biz.oneilenterprise.website.entity.Album;
import biz.oneilenterprise.website.entity.Media;
import biz.oneilenterprise.website.entity.PublicMediaApproval;
import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.enums.MediaType;
import biz.oneilenterprise.website.exception.AlbumMissingException;
import biz.oneilenterprise.website.exception.MediaApprovalException;
import biz.oneilenterprise.website.exception.MediaException;
import biz.oneilenterprise.website.utils.FileHandlerUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

public class MediaServiceTest extends BaseIntegrationTest {

    private static final String UNLISTED = "unlisted";
    private static final String PUBLIC = "public";
    public static final String PENDING = "pending";

    private final String mediaDirectory = "images/";
    private final String thumbnailDirectoryPath = mediaDirectory + "/thumbnail";

    private final File videoFile = new File("src/test/resources/video.mp4");
    private final File imageFile = new File("src/test/resources/image.png");
    private final User testUser = new User(71, "user1", "ROLE_UNREGISTERED");

    @Autowired
    private MediaService mediaService;

    @BeforeEach
    public void setup() {
        File file = new File(mediaDirectory);

        file.mkdir();
        File thumbnailDirectory = new File(thumbnailDirectoryPath);
        thumbnailDirectory.mkdir();
    }

    @AfterEach
    public void cleanup() {
        FileHandlerUtil.deleteDirectory(mediaDirectory);
    }

    @Test
    public void registerPublicMediasTest() throws IOException {
        MediaUploadDTO mediaUploadDTO = new MediaUploadDTO();

        List<MediaDTO> mediaDTOS = mediaService.registerMedias(Collections.singletonList(imageFile), mediaUploadDTO, testUser);

        assertThat(mediaDTOS).isNotEmpty();
        assertThat(mediaDTOS).size().isEqualTo(1);
        assertThat(mediaDTOS.get(0).getLinkStatus()).isEqualTo(UNLISTED);
        assertThat(mediaDTOS.get(0).getUploader()).isEqualTo(testUser.getUsername());
        assertThat(mediaDTOS.get(0).getMediaType()).isEqualTo(MediaType.IMAGE.toString());
    }

    @Test
    public void writeImageMediaThumbnailsTest() {
        mediaService.writeMediaThumbnails(Collections.singletonList(imageFile), testUser.getUsername());

        File imageThumbnail = new File(String.format("%s/%s/%s", thumbnailDirectoryPath, testUser.getUsername(), imageFile.getName()));
        assertThat(imageThumbnail).exists();
    }

    @Test
    public void writeVideoMediaThumbnailsTest() {
        mediaService.writeMediaThumbnails(Collections.singletonList(videoFile), testUser.getUsername());

        File videoThumbnail = new File(String.format("%s/%s/%s.png", thumbnailDirectoryPath, testUser.getUsername(), videoFile.getName()));
        assertThat(videoThumbnail).exists();
    }

    @Test
    public void updateMediaNameTest() {
        MediaUploadDTO mediaUploadDTO = new MediaUploadDTO();
        mediaUploadDTO.setName("Test");

        mediaService.updateMedia(mediaUploadDTO, 3);

        Media media = mediaService.getMedia(3);

        assertThat(media.getName()).isEqualTo("Test");
    }

    @Test
    public void updateMediaAlbumTest() {
        MediaUploadDTO mediaUploadDTO = new MediaUploadDTO();
        mediaUploadDTO.setAlbumId("test");

        mediaService.updateMedia(mediaUploadDTO, 3);

        Media media = mediaService.getMedia(3);

        assertThat(media.getAlbum()).isNotNull();
        assertThat(media.getAlbum().getName()).isEqualTo("test");
    }

    @Test
    public void getMediasTest() {
        HashMap<String, Object> medias = mediaService.getMedias(PageRequest.of(0, 10));

        assertThat((Long) medias.get("total")).isEqualTo(30L);
        assertThat((ArrayList<MediaDTO>) medias.get("medias")).size().isEqualTo(10);
    }

    @Test
    public void getPublicMediasTest() {
        HashMap<String, Object> medias = mediaService.getPublicMedias(PageRequest.of(0, 10));
        ArrayList<MediaDTO> mediaDTOS = (ArrayList<MediaDTO>) medias.get("medias");

        assertThat((Long) medias.get("total")).isEqualTo(5L);
        assertThat(mediaDTOS).size().isEqualTo(5);
        mediaDTOS.forEach(mediaDTO -> assertThat(mediaDTO.getLinkStatus()).isEqualTo(PUBLIC));
    }

    @Test
    public void getMediasByUserTest() {
        HashMap<String, Object> medias = mediaService.getMediasByUser(testUser.getUsername(), PageRequest.of(0, 10));
        ArrayList<MediaDTO> mediaDTOS = (ArrayList<MediaDTO>) medias.get("medias");

        assertThat((Long) medias.get("total")).isEqualTo(5L);
        assertThat(mediaDTOS).size().isEqualTo(5);
        mediaDTOS.forEach(mediaDTO -> assertThat(mediaDTO.getUploader()).isEqualTo(testUser.getUsername()));
    }

    @Test
    public void getMediaTest() {
        Media media = mediaService.getMedia(3);

        assertThat(media).isNotNull();
        assertThat(media.getId()).isEqualTo(3);
    }

    @Test
    public void getMediaNotFoundTest() {
        assertThatThrownBy(() -> mediaService.getMedia(30000))
            .isExactlyInstanceOf(MediaException.class)
            .hasMessage("Media does not exist on this server");
    }

    @Test
    public void getMediaFileNameTest() {
        Media media = mediaService.getMediaFileName("uYOHAkEEu00SCR7u.png");

        assertThat(media).isNotNull();
        assertThat(media.getFileName()).isEqualTo("uYOHAkEEu00SCR7u.png");
    }

    @Test
    public void registerMediaTest() throws IOException {
        Media media = mediaService.registerMedia(imageFile.getName(), UNLISTED, imageFile, testUser, null, imageFile.length());

        assertThat(media).isNotNull();
        assertThat(media.getLinkStatus()).isEqualTo(UNLISTED);
        assertThat(media.getUploader().getUsername()).isEqualTo(testUser.getUsername());
        assertThat(media.getSize()).isEqualTo(imageFile.length());
    }

    @Test
    public void getMediaApprovalByMediaIDTest() {
        Media media = mediaService.getMediaApprovalByMediaID(12);

        assertThat(media).isNotNull();
        assertThat(media.getPublicMediaApproval()).isNotNull();
    }

    @Test
    public void getMediaApprovalByMediaIDNotFoundTest() {
        assertThatThrownBy(() -> mediaService.getMediaApprovalByMediaID(56))
            .isExactlyInstanceOf(MediaException.class)
            .hasMessage("Media does not exist on this server");
    }

    @Test
    public void getMediaApprovalsByStatusTest() {
        List<MediaDTO> mediaApprovals = mediaService.getMediaApprovalsByStatus(PENDING);

        assertThat(mediaApprovals).size().isEqualTo(4);
        mediaApprovals.forEach(mediaDTO -> assertThat(mediaDTO.getPublicMediaApproval().getStatus()).isEqualTo(PENDING));
    }

    @Test
    public void requestPublicApprovalTest() {
        Media media = new Media("test.png", "test.png", UNLISTED, testUser, "05/05/2000", 5000L);

        PublicMediaApproval mediaApproval = mediaService.requestPublicApproval(media);

        assertThat(mediaApproval).isNotNull();
        assertThat(mediaApproval.getMedia()).isNotNull();
        assertThat(mediaApproval.getStatus()).isEqualTo(PENDING);
    }

    @Test
    public void requestPublicApprovalAgainTest() {
        Media media = new Media();
        media.setId(12);
        media.setPublicMediaApproval(new PublicMediaApproval(media, "pending"));

        assertThatThrownBy(() -> mediaService.requestPublicApproval(media))
            .isExactlyInstanceOf(MediaApprovalException.class)
            .hasMessage("This media has previously requested public access. Approval status: pending");
    }

    @Test
    public void setMediaApprovalStatusTest() {
        mediaService.setMediaApprovalStatus(12, "denied");

        Media media = mediaService.getMediaApprovalByMediaID(12);

        assertThat(media.getPublicMediaApproval().getStatus()).isEqualTo("denied");
    }

    @Test
    public void setMediaApprovalStatusNotExistingTest() {
        assertThatThrownBy(() ->mediaService.setMediaApprovalStatus(152, "denied"))
            .isExactlyInstanceOf(MediaException.class)
            .hasMessage("Media does not exist on this server");
    }

    @Test
    public void approvePublicMediaTest() {
        mediaService.approvePublicMedia(12);

        assertThatThrownBy(() -> mediaService.getMediaApprovalByMediaID(12))
            .isExactlyInstanceOf(MediaApprovalException.class)
            .hasMessage("Media approval item not found");
    }

    @Test
    public void approvePublicMediaNotExistingTest() {
        assertThatThrownBy(() -> mediaService.approvePublicMedia(152))
            .isExactlyInstanceOf(MediaException.class)
            .hasMessage("Media does not exist on this server");
    }

    @Test
    public void getTotalMediasTest() {
        long total = mediaService.getTotalMedias();

        assertThat(total).isEqualTo(30L);
    }

    @Test
    public void getUserMediaDirectoryTest() {
        String userMediaDirectory = mediaService.getUserMediaDirectory(testUser.getUsername());

        assertThat(userMediaDirectory).isEqualTo(mediaDirectory + testUser.getUsername() + "/");
    }

    @NotNull
    private File writeMediaFileForTests() throws IOException {
        FileHandlerUtil.writeImageThumbnail(imageFile, mediaService.getUserMediaDirectory(testUser.getUsername()));
        File file = new File(mediaService.getUserMediaDirectory(testUser.getUsername()) + imageFile.getName());

        assertThat(file).exists();
        return file;
    }

    @Test
    public void deleteMediasTest() throws IOException {
        Integer[] medias = new Integer[]{1, 2, 3, 4};
        File file = writeMediaFileForTests();

        Long deletedSize = mediaService.deleteMedias(medias);
        List<Media> mediaList = mediaService.getMediasByIds(medias);

        assertThat(file).doesNotExist();
        assertThat(deletedSize).isEqualTo(432944L);
        assertThat(mediaList).isEmpty();
    }

    public void updateMediasLinkStatusTest() {

    }

    @Test
    public void getMediaFileTest() throws IOException {
        File file = writeMediaFileForTests();

        File mediaFile = mediaService.getMediaFile("image.png");

        assertThat(mediaFile).exists();
        assertThat(mediaFile).isFile();
        assertThat(mediaFile).hasSameBinaryContentAs(file);
        assertThat(mediaFile).hasName(file.getName());
    }

    @Test
    public void getMediaFileNotExistsTest() {
        File mediaFile = mediaService.getMediaFile("image.png");

        assertThat(mediaFile).isNotNull();
        assertThat(mediaFile).hasName("noimage.png");
    }

    @Test
    public void getMediaThumbnailFileTest() {
        mediaService.writeMediaThumbnails(Collections.singletonList(imageFile), testUser.getUsername());

        File mediaThumbnail = mediaService.getMediaThumbnailFile("image.png");

        assertThat(mediaThumbnail).exists();
        assertThat(mediaThumbnail).isFile();
    }

    @Test
    public void getMediaThumbnailFileNotExistsTest() {
        File mediaThumbnail = mediaService.getMediaThumbnailFile("image.png");

        assertThat(mediaThumbnail).isNotNull();
        assertThat(mediaThumbnail).hasName("noimage.png");
    }

    @Test
    public void getMediaStatsTest() {
        HashMap<String, Object> stats = mediaService.getMediaStats(testUser.getUsername());

        assertThat(stats).containsKey("totalMedias").containsValue(5L);
        assertThat(stats).containsKey("totalAlbums").containsValue(0L);
        assertThat(stats).containsKey("recentMedias");
    }

    @Test
    public void getOrRegisterAlbumTest() {
        Album album = mediaService.getOrRegisterAlbum("", "albumID");

        assertThat(album).isNotNull();
        assertThat(album.getCreator()).isEqualTo("albumCreator");
        assertThat(album.getMedias()).size().isEqualTo(1);
    }

    @Test
    public void getOrRegisterAlbumNotExistsTest() {
        Album album = mediaService.getOrRegisterAlbum(testUser.getUsername(), "awds");

        assertThat(album).isNotNull();
        assertThat(album.getCreator()).isEqualTo(testUser.getUsername());
        assertThat(album.getMedias()).isEmpty();
        assertThat(album.getName()).isEqualTo("awds");
    }

    @Test
    public void registerNewAlbumTest() {
        Album album = mediaService.registerNewAlbum("test", testUser.getUsername());
        Album wasAlbumSaved = mediaService.getAlbum(album.getId());

        assertThat(album.getName()).isEqualTo("test");
        assertThat(album.getCreator()).isEqualTo(testUser.getUsername());
        assertThat(wasAlbumSaved).isNotNull();
    }

    @Test
    public void resetMediaAlbumIDsTest() {
        Album album = mediaService.getAlbum("albumID");

        mediaService.resetMediaAlbumIDs(album);
        Media media = mediaService.getMedia(album.getMedias().get(0).getId());

        assertThat(media.getAlbum()).isNull();
    }

    @Test
    public void getAlbumTest() {
        Album album = mediaService.getAlbum("albumID");

        assertThat(album).isNotNull();
        assertThat(album.getCreator()).isEqualTo("albumCreator");
        assertThat(album.getName()).isEqualTo("albumName");
    }

    @Test
    public void getAlbumNotExistsTest() {
        assertThatThrownBy(() -> mediaService.getAlbum("notFound"))
            .isExactlyInstanceOf(AlbumMissingException.class)
            .hasMessage("Album not found");
    }

    @Test
    public void getAlbumWithMediasTest() {
        Album album = mediaService.getAlbumWithMedias("albumID");

        assertThat(album).isNotNull();
        assertThat(album.getCreator()).isEqualTo("albumCreator");
        assertThat(album.getName()).isEqualTo("albumName");
        assertThat(album.getMedias()).isNotEmpty();
    }

    @Test
    public void getAlbumWithMediasNotExistsTest() {
        assertThatThrownBy(() -> mediaService.getAlbumWithMedias("notFound"))
            .isExactlyInstanceOf(AlbumMissingException.class)
            .hasMessage("Album not found");
    }

    @Test
    public void getPublicAlbumTest() {
        AlbumDTO albumDTO = mediaService.getPublicAlbum("albumID");

        assertThat(albumDTO).isNotNull();
        assertThat(albumDTO.getMedias()).isNotEmpty();
        assertThat(albumDTO.getName()).isEqualTo("albumName");
    }

    @Test
    public void getPublicAlbumNotExistsTest() {
        assertThatThrownBy(() -> mediaService.getPublicAlbum("notFound"))
            .isExactlyInstanceOf(AlbumMissingException.class)
            .hasMessage("Album not found");
    }

    @Test
    public void deleteAlbumTest() {
        mediaService.deleteAlbum("albumID");
        Media media = mediaService.getMedia(1);

        assertThatThrownBy(() -> mediaService.getAlbumWithMedias("notFound"))
            .isExactlyInstanceOf(AlbumMissingException.class)
            .hasMessage("Album not found");
        assertThat(media.getAlbum()).isNull();
    }

    @Test
    public void deleteAlbumNotExistsTest() {
        assertThatThrownBy(() -> mediaService.deleteAlbum("notFound"))
            .isExactlyInstanceOf(AlbumMissingException.class)
            .hasMessage("Album not found");
    }

    @Test
    public void getAlbumsByUserTest() {
        List<AlbumDTO> albumDTOS = mediaService.getAlbumsByUser("albumCreator");

        assertThat(albumDTOS).isNotEmpty();
    }

    @Test
    public void getAlbumsByUserNoAlbumsTest() {
        List<AlbumDTO> albumDTOS = mediaService.getAlbumsByUser("nouser");

        assertThat(albumDTOS).isEmpty();
    }

    @Test
    public void getTotalAlbumsTest() {
        long totalAlbums = mediaService.getTotalAlbums();

        assertThat(totalAlbums).isEqualTo(2L);
    }

    @Test
    public void updateAlbumTest() {
        mediaService.updateAlbum("albumID", "test_name");

        Album album = mediaService.getAlbum("albumID");

        assertThat(album.getName()).isEqualTo("test_name");
    }

    @Test
    public void updateAlbumNotFoundTest() {
        assertThatThrownBy(() -> mediaService.updateAlbum("noAlbum", "test_name"))
            .isExactlyInstanceOf(AlbumMissingException.class)
            .hasMessage("Album not found");
    }

    @Test
    public void addMediasToAlbumTest() {
        int[] mediaIds = {2, 3, 4, 5};
        mediaService.addMediasToAlbum("albumID", mediaIds, testUser.getUsername());

        List<Media> medias = mediaService.getMediasByIds(new Integer[]{2, 3, 4, 5});

        assertThat(medias).isNotEmpty();
        medias.forEach(media -> assertThat(media.getAlbum()).isNotNull());
    }

    @Test
    public void getMediasByIdsTest() {
        Integer[] mediaIds = {1, 2, 3, 4};
        List<Media> medias = mediaService.getMediasByIds(mediaIds);

        assertThat(medias).isNotEmpty();
        assertThat(medias).size().isEqualTo(mediaIds.length);
    }

    @Test
    public void getMediasByIdsNotExistsTest() {
        Integer[] mediaIds = {100, 200, 300, 400};
        List<Media> medias = mediaService.getMediasByIds(mediaIds);

        assertThat(medias).isEmpty();
    }

    @Test
    public void albumToDTOsTest() {
        Album album = new Album("test", "test", "test");
        Media media = new Media();
        album.setMedias(Collections.singletonList(media));

        List<AlbumDTO> albumDTOS = mediaService.albumToDTOs(Collections.singletonList(album));

        assertThat(albumDTOS).isNotEmpty();

        AlbumDTO albumDTO = albumDTOS.get(0);

        assertThat(albumDTO.getName()).isEqualTo(album.getName());
        assertThat(albumDTO.getCreator()).isEqualTo(album.getCreator());
        assertThat(albumDTO.getId()).isEqualTo(album.getId());
        assertThat(albumDTO.getMedias()).isNotEmpty();
    }

    @Test
    public void mediaToDTOsTest() {
        Media media = new Media("test", "test", "test", testUser, "test", 10L);
        List<MediaDTO> mediaDTOS = mediaService.mediaToDTOs(Collections.singletonList(media));

        assertThat(mediaDTOS).isNotEmpty();

        MediaDTO mediaDTO = mediaDTOS.get(0);

        assertThat(mediaDTO.getId()).isEqualTo(media.getId());
        assertThat(mediaDTO.getUploader()).isEqualTo(media.getUploader().getUsername());
        assertThat(mediaDTO.getLinkStatus()).isEqualTo(media.getLinkStatus());
        assertThat(mediaDTO.getFileName()).isEqualTo(media.getFileName());
        assertThat(mediaDTO.getSize()).isEqualTo(media.getSize());
        assertThat(mediaDTO.getDateAdded()).isEqualTo(media.getDateAdded());
    }
}
