package biz.oneilenterprise.website.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import biz.oneilenterprise.website.exception.MediaException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileHandlerUtilTest {

    private File file;
    private static File testDirectory;
    private final File videoFile = new File("src/test/resources/video.mp4");
    private final File imageFile = new File("src/test/resources/image.png");

    @BeforeEach
    public void setup() {
        testDirectory = new File("test/");
        file = new File(testDirectory.getAbsolutePath() + "/test.txt");
        try {
            testDirectory.mkdir();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void cleanUp() {
        if (testDirectory.exists()) {
            FileHandlerUtil.deleteDirectory(testDirectory.getAbsolutePath());
        }
    }

    @Test
    public void writeFileTest() {
        try (InputStream inputStream = new FileInputStream(file)) {
            File newFile = FileHandlerUtil.writeFile(inputStream, "newFile.txt", testDirectory.getAbsolutePath() + "\\");

            assertThat(newFile).exists();
            assertThat(newFile.getName()).isEqualTo("newFile.txt");
            assertThat(newFile.getAbsolutePath()).isEqualTo(testDirectory.getAbsolutePath() + "\\newFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeFileWithExistingNameTest() {
        try (InputStream inputStream = new FileInputStream(file)) {
            File newFile = FileHandlerUtil.writeFile(inputStream, file.getName(), testDirectory.getAbsolutePath() + "\\");

            assertThat(newFile).exists();
            assertThat(newFile.getName()).isEqualTo("test(1).txt");
            assertThat(newFile.getAbsolutePath()).isEqualTo(testDirectory.getAbsolutePath() + "\\test(1).txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void moveNewFilesToDirectory() {
        File newDirectory = new File(testDirectory.getAbsolutePath() + "/move");
        newDirectory.mkdir();

        FileHandlerUtil.moveNewFilesToDirectory(Collections.singletonList(file), newDirectory.getAbsolutePath());

        File[] childFiles = newDirectory.listFiles();

        assertThat(childFiles.length).isEqualTo(1);
        assertThat(new File(testDirectory.getAbsolutePath() + "/move/" + file.getName() )).exists();
    }

    @Test
    public void deleteFileTest() {
        FileHandlerUtil.deleteFile(file.getPath());

        assertThat(file.exists()).isFalse();
    }

    @Test
    public void deleteDirectoryTest() {
        FileHandlerUtil.deleteDirectory(testDirectory.getAbsolutePath());

        assertThat(testDirectory.exists()).isFalse();
    }

    @Test
    public void writeImageThumbnailTest() throws IOException {
        FileHandlerUtil.writeImageThumbnail(imageFile, testDirectory.getAbsolutePath() + "/thumbnails");

        File thumbnail = new File(testDirectory.getAbsolutePath() + "/thumbnails/" + imageFile.getName());

        assertThat(thumbnail).exists();
    }

    @Test
    public void writeVideoThumbnailTest() {
        File thumbnailDirectory = new File(testDirectory.getAbsolutePath() + "/thumbnails");
        thumbnailDirectory.mkdir();

        FileHandlerUtil.writeVideoThumbnail(videoFile, thumbnailDirectory.getAbsolutePath());

        File thumbnail = new File(testDirectory.getAbsolutePath() + "/thumbnails/" + videoFile.getName() + ".png");

        assertThat(thumbnail).exists();
    }

    @Test
    public void getExtensionTypeTest() {
        String extension = FileHandlerUtil.getExtensionType(file.getName());

        assertThat(extension).isEqualTo("txt");
    }

    @Test
    public void getContentTypeTest() {
        String extension = FileHandlerUtil.getExtensionType(file.getName());

        assertThat(extension).isEqualTo("txt");
    }

    @Test
    public void renameFileTest() {
        File renamedFile = FileHandlerUtil.renameFile(file, testDirectory.getAbsolutePath());

        assertThat(renamedFile.getName()).isEqualTo("test(1).txt");
        assertThat(renamedFile.getAbsolutePath()).isEqualTo(testDirectory.getAbsolutePath() + "\\test(1).txt");
    }

    @Test
    public void isImageFileTest() {
        boolean isImage = FileHandlerUtil.isImageFile("image.png");

        assertThat(isImage).isTrue();
    }

    @Test
    public void isNotImageFileTest() {
        boolean isNotImage = FileHandlerUtil.isImageFile(file.getName());

        assertThat(isNotImage).isFalse();
    }

    @Test
    public void isVideoFile() throws IOException {
        boolean isVideoFile = FileHandlerUtil.isVideoFile(videoFile);

        assertThat(isVideoFile).isTrue();
    }

    @Test
    public void isNotVideoFile() throws IOException {
        boolean isNotVideo = FileHandlerUtil.isVideoFile(file);

        assertThat(isNotVideo).isFalse();
    }

    @Test
    public void getFileMediaTypeVideoTest() throws IOException {
        String mediaType = FileHandlerUtil.getFileMediaType(videoFile);

        assertThat(mediaType).isEqualTo("VIDEO");
    }

    @Test
    public void getFileMediaTypeImageTest() throws IOException {
        String mediaType = FileHandlerUtil.getFileMediaType(imageFile);

        assertThat(mediaType).isEqualTo("IMAGE");
    }

    @Test
    public void getFileMediaTypeInvalidTest() {
        assertThatThrownBy(() -> FileHandlerUtil.getFileMediaType(file))
            .isExactlyInstanceOf(MediaException.class)
            .hasMessage("Not a valid media file");
    }
}
