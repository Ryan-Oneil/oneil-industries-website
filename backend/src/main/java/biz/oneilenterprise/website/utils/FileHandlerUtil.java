package biz.oneilenterprise.website.utils;

import biz.oneilenterprise.website.enums.MediaType;
import biz.oneilenterprise.website.exception.MediaException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

public class FileHandlerUtil {

    private static final Logger logger = LogManager.getLogger(FileHandlerUtil.class);
    private static final List<String> supportImageFormats;

    static {
        supportImageFormats = Arrays.asList(ImageIO.getWriterFormatNames());
    }

    private FileHandlerUtil() {}

    public static File writeFile(InputStream inputStream, String fileName, String dest) throws IOException {
        File destFolder = new File(dest);

        if (!destFolder.exists()) {
            destFolder.mkdir();
        }
        File newFile = new File(dest + "/" + fileName);

        if (newFile.exists()) {
            newFile = renameFile(newFile, dest);
        }
        //Copy file to new file
        FileUtils.copyInputStreamToFile(inputStream, newFile);

        return newFile;
    }

    public static void moveNewFilesToDirectory(List<File> files, String dest) {
        File destFolder = new File(dest);
        File originalParent = files.get(0).getParentFile();

        files.forEach(file -> {
            try {
                File newFile = new File(dest + "/" + file.getName());

                //Renames the original file
                if (newFile.exists()) {
                    String newFileName = renameFile(file, dest).getName();
                    File renamedFile = new File(originalParent.getAbsolutePath() + "/" + newFileName);
                    file.renameTo(renamedFile);
                    file = renamedFile;
                }
                FileUtils.moveFileToDirectory(file, destFolder, false);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        });
        try {
            Files.deleteIfExists(originalParent.toPath());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void deleteFile(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void deleteDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] childrenFiles = directory.listFiles();

        if (childrenFiles == null) {
            logger.error("{}: Not a directory or doesn't exist", directoryPath);
            return;
        }

        for (File childrenFile : childrenFiles) {
            if (childrenFile.isDirectory()) {
                deleteDirectory(childrenFile.getPath());
            }
            deleteFile(childrenFile.getPath());
        }
        deleteFile(directory.getPath());
    }

    public static void writeImageThumbnail(File file, String dest) throws IOException {
        String extension = getExtensionType(file.getName());

        ImageThumbnailWriterUtil.writeImage(file, dest, extension);
    }

    public static void writeVideoThumbnail(File video, String dest) {
        ImageThumbnailWriterUtil.writeThumbnailFromVideo(video, dest);
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
        String fileExtension = extension;

        if (extension.contains(".")) fileExtension = getExtensionType(extension);

        return supportImageFormats.contains(fileExtension);
    }

    public static boolean isVideoFile(File file) throws IOException {
        Tika tika = new Tika();
        String contentType = tika.detect(file);

        return contentType.startsWith("video");
    }

    public static String getFileMediaType(File file) throws IOException {
        if (isImageFile(file.getName())) {
            return MediaType.IMAGE.toString();
        } else if (isVideoFile(file)) {
            return MediaType.VIDEO.toString();
        }
        throw new MediaException("Not a valid media file");
    }
}
