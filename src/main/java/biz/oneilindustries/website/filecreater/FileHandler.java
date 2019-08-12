package biz.oneilindustries.website.filecreater;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class FileHandler {

    private static List<String> supportImageFormats;

    static {
        supportImageFormats = Arrays.asList(ImageIO.getWriterFormatNames());
    }

    private FileHandler() {
    }

    public static void writeFile(MultipartFile file, String dest) throws IOException {
        String extension = getExtensionType(file.getOriginalFilename());

        //If file is image
        if (isImageFile(extension)) {
            ImageThumbnailWriter.writeImage(file,dest, extension);
        }

        File newFile = new File(dest + file.getOriginalFilename());
        //Copy file to new file
        file.transferTo(newFile);
    }

    public static String getExtensionType(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf('.')+1).toLowerCase();
    }

    public static String getContentType(String originalFileName) {
        String extensionType = getExtensionType(originalFileName);

        if (extensionType.equalsIgnoreCase("jpg") || extensionType.equalsIgnoreCase("jpeg")) return "jpeg";

        return extensionType;
    }

    public static boolean isImageFile(String extension) {

        if (extension.contains(".")) extension = getExtensionType(extension);

        return supportImageFormats.contains(extension);
    }

    public static boolean isVideoFile(String contentType) {
        return contentType.startsWith("video");
    }
}
