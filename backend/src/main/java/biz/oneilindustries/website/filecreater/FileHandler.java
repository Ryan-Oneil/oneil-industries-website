package biz.oneilindustries.website.filecreater;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.FileUtils;

public class FileHandler {

    private static List<String> supportImageFormats;

    static {
        supportImageFormats = Arrays.asList(ImageIO.getWriterFormatNames());
    }

    private FileHandler() {
    }

    public static File writeFile(FileItemStream file, String orignalFileName, String dest, String uploader) throws IOException {
        String extension = getExtensionType(orignalFileName);

        String fileName = UUID.randomUUID().toString() + "." + extension;

        File destFolder = new File(dest + uploader + "/");

        if (!destFolder.exists()) {
            destFolder.mkdir();
        }
        File newFile = new File(dest + uploader + "/" + fileName);

        //Checks the randomly generated file name doesn't already exists and keeps changing till string is unique
        while (newFile.exists()) {
            newFile = new File(dest + uploader + "/" + UUID.randomUUID().toString());
        }
        //Copy file to new file
        FileUtils.copyInputStreamToFile(file.openStream(), newFile);

        //If file is image
        if (isImageFile(extension)) {
            ImageThumbnailWriter.writeImage(newFile,dest, extension, uploader);
        }
        return newFile;
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
