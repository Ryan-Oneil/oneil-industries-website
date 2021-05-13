package biz.oneilenterprise.website.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import biz.oneilenterprise.website.dto.GalleryUploadDTO;
import biz.oneilenterprise.website.dto.MediaDTO;
import biz.oneilenterprise.website.entity.Media;
import biz.oneilenterprise.website.entity.PublicMediaApproval;
import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.enums.MediaType;
import biz.oneilenterprise.website.exception.MediaApprovalException;
import biz.oneilenterprise.website.exception.MediaException;
import biz.oneilenterprise.website.utils.FileHandlerUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({"/testData.sql"})
@Sql(scripts = "/delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class MediaServiceTest {

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
        GalleryUploadDTO galleryUploadDTO = new GalleryUploadDTO();
        galleryUploadDTO.setPrivacy(UNLISTED);

        List<MediaDTO> mediaDTOS = mediaService.registerMedias(Collections.singletonList(imageFile), galleryUploadDTO, testUser);

        assertThat(mediaDTOS).isNotEmpty();
        assertThat(mediaDTOS).size().isEqualTo(1);
        assertThat(mediaDTOS.get(0).getLinkStatus()).isEqualTo(UNLISTED);
        assertThat(mediaDTOS.get(0).getUploader()).isEqualTo(testUser.getUsername());
        assertThat(mediaDTOS.get(0).getMediaType()).isEqualTo(MediaType.IMAGE.toString());
    }

    @Test
    public void registerUnlistedMediasTest() throws IOException {
        GalleryUploadDTO galleryUploadDTO = new GalleryUploadDTO();
        galleryUploadDTO.setPrivacy(PUBLIC);

        List<MediaDTO> mediaDTOS = mediaService.registerMedias(Collections.singletonList(imageFile), galleryUploadDTO, testUser);

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
    public void checkPublicMediaPrivacyTest() {
        Media media = new Media("test.png", "test.png", PUBLIC, testUser, "05/05/2000", 5000L);

        mediaService.checkMediaPrivacy(media, testUser);

        assertThat(media.getPublicMediaApproval()).isNotNull();
        assertThat(media.getPublicMediaApproval().getStatus()).isEqualTo(PENDING);
        assertThat(media.getLinkStatus()).isEqualTo(UNLISTED);
    }

    @Test
    public void checkUnlistedMediaPrivacyTest() {
        Media media = new Media("test.png", "test.png", UNLISTED, testUser, "05/05/2000", 5000L);

        mediaService.checkMediaPrivacy(media, testUser);

        PublicMediaApproval publicMediaApproval = mediaService.getMediaApprovalByMediaID(media.getId());

        assertThat(publicMediaApproval).isNull();
    }

    @Test
    public void updateMediaNameTest() {
        GalleryUploadDTO galleryUploadDTO = new GalleryUploadDTO();
        galleryUploadDTO.setName("Test");

        mediaService.updateMedia(galleryUploadDTO, 3, testUser);

        Media media = mediaService.getMedia(3);

        assertThat(media.getName()).isEqualTo("Test");
    }

    @Test
    public void updateMediaAlbumTest() {
        GalleryUploadDTO galleryUploadDTO = new GalleryUploadDTO();
        galleryUploadDTO.setAlbumId("test");

        mediaService.updateMedia(galleryUploadDTO, 3, testUser);

        Media media = mediaService.getMedia(3);

        assertThat(media.getAlbum()).isNotNull();
        assertThat(media.getAlbum().getName()).isEqualTo("test");
    }

    @Test
    public void updateMediaStatusTest() {
        GalleryUploadDTO galleryUploadDTO = new GalleryUploadDTO();
        galleryUploadDTO.setPrivacy(PUBLIC);

        mediaService.updateMedia(galleryUploadDTO, 3, testUser);

        PublicMediaApproval publicMediaApproval = mediaService.getMediaApprovalByMediaID(3);

        assertThat(publicMediaApproval).isNotNull();
        assertThat(publicMediaApproval.getMedia().getLinkStatus()).isEqualTo(UNLISTED);
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
    public void saveMediaApprovalTest() {
        PublicMediaApproval publicMediaApproval = mediaService.getMediaApprovalByMediaID(12);
        publicMediaApproval.setStatus("denied");
        mediaService.saveMediaApproval(publicMediaApproval);

        publicMediaApproval = mediaService.getMediaApprovalByMediaID(12);

        assertThat(publicMediaApproval.getStatus()).isEqualTo("denied");
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
        PublicMediaApproval publicMediaApproval = mediaService.getMediaApprovalByMediaID(12);

        assertThat(publicMediaApproval).isNotNull();
        assertThat(publicMediaApproval.getMedia()).isNotNull();
    }

    @Test
    public void getMediaApprovalByMediaIDNotFoundTest() {
        PublicMediaApproval publicMediaApproval = mediaService.getMediaApprovalByMediaID(12);

        assertThat(publicMediaApproval).isNull();
    }

    @Test
    public void getMediaApprovalsByStatusTest() {
        List<PublicMediaApproval> mediaApprovals = mediaService.getMediaApprovalsByStatus(PENDING);

        assertThat(mediaApprovals).size().isEqualTo(4);
        mediaApprovals.forEach(publicMediaApproval -> assertThat(publicMediaApproval.getStatus()).isEqualTo(PENDING));
    }

    @Test
    public void requestPublicApprovalTest() {
        Media media = new Media("test.png", "test.png", UNLISTED, testUser, "05/05/2000", 5000L);

        PublicMediaApproval mediaApproval = mediaService.requestPublicApproval(media, "test.png");

        assertThat(mediaApproval).isNotNull();
        assertThat(mediaApproval.getMedia()).isNotNull();
        assertThat(mediaApproval.getStatus()).isEqualTo(PENDING);
    }

    @Test
    public void requestPublicApprovalAgainTest() {
        Media media = new Media();
        media.setId(12);


        assertThatThrownBy(() -> mediaService.requestPublicApproval(media, "test.png"))
            .isExactlyInstanceOf(MediaApprovalException.class)
            .hasMessage("This media has previously requested public access. Approval status: pending");
    }

    @Test
    public void setMediaApprovalStatusTest() {
        mediaService.setMediaApprovalStatus(12, "denied");

        PublicMediaApproval mediaApproval = mediaService.getMediaApprovalByMediaID(12);

        assertThat(mediaApproval.getStatus()).isEqualTo("denied");
    }

    @Test
    public void setMediaApprovalStatusNotExistingTest() {
        assertThatThrownBy(() ->mediaService.setMediaApprovalStatus(152, "denied"))
            .isExactlyInstanceOf(MediaApprovalException.class)
            .hasMessage("Media approval item not found");
    }

    @Test
    public void approvePublicMediaTest() {
        mediaService.approvePublicMedia(12);

        PublicMediaApproval publicMediaApproval = mediaService.getMediaApprovalByMediaID(12);

        assertThat(publicMediaApproval).isNull();
    }

    @Test
    public void approvePublicMediaNotExistingTest() {
        assertThatThrownBy(() -> mediaService.approvePublicMedia(152))
            .isExactlyInstanceOf(MediaApprovalException.class)
            .hasMessage("No media approval found for media id 152");
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

    @Test
    public void deleteMediaTest() throws IOException {
        FileHandlerUtil.writeImageThumbnail(imageFile, mediaService.getUserMediaDirectory(testUser.getUsername()));
        File file = new File(mediaService.getUserMediaDirectory(testUser.getUsername()) + imageFile.getName());

        assertThat(file).exists();

        Media media = mediaService.getMedia(4);

        long deletedMediaSize = mediaService.deleteMedia(4);

        assertThat(file).doesNotExist();
        assertThat(deletedMediaSize).isEqualTo(media.getSize());
        assertThatThrownBy(() -> mediaService.getMedia(4))
            .isExactlyInstanceOf(MediaException.class)
            .hasMessage("Media does not exist on this server");
    }
}
