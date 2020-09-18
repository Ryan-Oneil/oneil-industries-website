package biz.oneilindustries.website.filecreater;

import biz.oneilindustries.website.exception.MediaException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

@Component
public class FileHandler {

    private static List<String> supportImageFormats;

    static {
        supportImageFormats = Arrays.asList(ImageIO.getWriterFormatNames());
    }

    private FileHandler() {
    }

    public static File writeFile(FileItemStream file, String fileName, String dest) throws IOException {
        File destFolder = new File(dest);

        if (!destFolder.exists()) {
            destFolder.mkdir();
        }
        File newFile = new File(dest + "/" + fileName);

        if (newFile.exists()) {
            newFile = renameFile(newFile, dest);
        }
        //Copy file to new file
        FileUtils.copyInputStreamToFile(file.openStream(), newFile);

        return newFile;
    }

    public static void writeImageThumbnail(File file, String dest) throws IOException {
        String extension = getExtensionType(file.getName());

        ImageThumbnailWriter.writeImage(file, dest, extension);
    }

    public static String getExtensionType(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf('.')+1).toLowerCase();
    }

    public static String getContentType(String originalFileName) {
        String extensionType = getExtensionType(originalFileName);

        if (extensionType.equalsIgnoreCase("jpg") || extensionType.equalsIgnoreCase("jpeg")) return "jpeg";

        return extensionType;
    }

    public static File renameFile(File file, String dest) {
        int fileCount = 1;
        File currentFile = file;

        while (currentFile.exists()) {
            String fileName = file.getName().substring(0, file.getName().lastIndexOf('.')) + String.format("(%s)", fileCount);
            String fileExtension = "." + getExtensionType(file.getName());

            currentFile = new File(String.format("%s/%s", dest, fileName + fileExtension));
            fileCount++;
        }
        return currentFile;
    }

    public static boolean isImageFile(String extension) {

        if (extension.contains(".")) extension = getExtensionType(extension);

        return supportImageFormats.contains(extension);
    }

    public static boolean isVideoFile(File file) throws IOException {
        Tika tika = new Tika();
        String contentType = tika.detect(file);

        return contentType.startsWith("video");
    }

    public static String getFileMediaType(File file) throws IOException {
        if (isImageFile(file.getName())) {
            return "image";
        } else if (isVideoFile(file)) {
            return "video";
        }
        throw new MediaException("Not a valid media file");
    }
}
