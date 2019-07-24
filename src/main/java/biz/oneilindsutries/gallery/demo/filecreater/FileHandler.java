package biz.oneilindsutries.gallery.demo.filecreater;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileHandler {

    private List<String> supportImageFormats;

    public FileHandler() {
        this.supportImageFormats = Arrays.asList(ImageIO.getWriterFormatNames());
    }

    public void writeFile(MultipartFile file, String dest) throws IOException {
        String extension = getExtensionType(file.getOriginalFilename());

        //If file is image
        if (isImageFile(extension)) {
            ImageThumbnailWriter.writeImage(file,dest, extension);
        }
        File newFile = new File(dest + file.getOriginalFilename());
        //Copy file to new file
        file.transferTo(newFile);
    }

    public String getExtensionType(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf('.')+1).toLowerCase();
    }

    public boolean isImageFile(String extension) {
        return supportImageFormats.contains(extension);
    }
}
