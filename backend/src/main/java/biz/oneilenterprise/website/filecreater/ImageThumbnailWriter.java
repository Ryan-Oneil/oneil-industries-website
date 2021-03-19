package biz.oneilenterprise.website.filecreater;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class ImageThumbnailWriter {

    private static final Logger logger = LogManager.getLogger(ImageThumbnailWriter.class);
    private static final int IMAGE_RESIZE_SIZE = 400;

    private ImageThumbnailWriter() {}

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
        g.drawImage(original, 0, 0, IMAGE_RESIZE_SIZE, IMAGE_RESIZE_SIZE, null);
        g.dispose();
        return thumbNail;
    }

    public static void writeThumbnailFromVideo(File video, String dest) {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(video.getPath());

        try {
            frameGrabber.start();
            Java2DFrameConverter frameConverter = new Java2DFrameConverter();
            Frame frame = frameGrabber.grabKeyFrame();
            BufferedImage bufferedImage = frameConverter.convert(frame);

            while (bufferedImage != null) {
                write(getThumbnailImage(bufferedImage), "png", new File(dest+ "/" + video.getName() + ".png"));

                frame = frameGrabber.grabKeyFrame();
                bufferedImage = frameConverter.convert(frame);
            }
        } catch (IOException e) {
            logger.error("Error creating video thumbnail", e);
        } finally {
            try {
                frameGrabber.stop();
            } catch (Exception e) {
                logger.error("Error creating video thumbnail", e);
            }
        }
    }
}
