package biz.oneilindustries.website.filecreater;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ImageThumbnailWriter {

    private ImageThumbnailWriter() {
    }

    private static final int IMAGE_RESIZE_SIZE = 400;

    public static void writeImage(MultipartFile image,String dest, String extension) throws IOException {
        BufferedImage src = ImageIO.read(new ByteArrayInputStream(image.getBytes()));

        File thumbnailDestination = new File(dest + "\\thumbnail\\" + image.getOriginalFilename());

        //Writes thumbnail
        write(getThumbnailImage(src),extension,thumbnailDestination);
    }

    public static void write(BufferedImage src, String imageFormat, File destination) throws IOException {
        ImageIO.write(src, imageFormat, destination);
    }

    private static BufferedImage getThumbnailImage(BufferedImage original) {
        BufferedImage thumbNail = new BufferedImage(
                IMAGE_RESIZE_SIZE, IMAGE_RESIZE_SIZE,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbNail.createGraphics();
        g.drawImage(original, 0, 0,IMAGE_RESIZE_SIZE,IMAGE_RESIZE_SIZE, null);
        g.dispose();
        return thumbNail;
    }
}
