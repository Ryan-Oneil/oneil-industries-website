package biz.oneilindustries.website.filecreater;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;

public class ImageThumbnailWriter {

    private ImageThumbnailWriter() {
    }

    private static final int IMAGE_RESIZE_SIZE = 400;

    public static void writeImage(File image, String dest, String extension) throws IOException {
        BufferedImage src = ImageIO.read(image);
        File destFolder = new File(dest);

        if (!destFolder.exists()) {
            Files.createDirectory(destFolder.toPath());
        }
        File thumbnailDestination = new File(dest + "/" + image.getName());

        //Writes thumbnail
        write(getThumbnailImage(src), extension, thumbnailDestination);
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
