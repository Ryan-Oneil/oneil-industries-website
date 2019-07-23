package biz.oneilindsutries.gallery.demo.filecreater;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

    private static final String IMAGE_LOCATIONS = "C:\\Users\\Ryan\\Desktop\\OneilIndustries\\src\\main\\webapp\\WEB-INF\\view\\gallery\\images\\";
    private static final int IMAGE_RESIZE_SIZE = 400;

    public static void writeImage(MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();
        String fileExtension = image.getOriginalFilename().substring(fileName.lastIndexOf(".")+1,fileName.length()).toLowerCase();

        BufferedImage src = ImageIO.read(new ByteArrayInputStream(image.getBytes()));

        File destination = new File(IMAGE_LOCATIONS + "\\" + image.getOriginalFilename());

        File thumbnailDestination = new File(IMAGE_LOCATIONS + "\\thumbnail\\" + image.getOriginalFilename());

        //Writes both files
        write(src,fileExtension,destination);
        write(getThumbnailImage(src),fileExtension,thumbnailDestination);
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
