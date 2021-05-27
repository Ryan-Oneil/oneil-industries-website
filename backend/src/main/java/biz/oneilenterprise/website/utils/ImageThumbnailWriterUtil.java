package biz.oneilenterprise.website.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageThumbnailWriterUtil {

    private final Logger logger = LogManager.getLogger(ImageThumbnailWriterUtil.class);
    private static final int IMAGE_RESIZE_SIZE = 400;

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    public ImageThumbnailWriterUtil(@Value("${video.ffmpeg.location}") String ffmpegPath, @Value("${video.ffprobe.location}") String ffprobePath) {
        try {
            ffmpeg = new FFmpeg(ffmpegPath);
            ffprobe = new FFprobe(ffprobePath);
        } catch (IOException e) {
            logger.error("Unable to create ImageThumbnailWriterUtil ", e);
        }
    }

    public void writeThumbnailFromMedia(File file, String dest) throws IOException {
        String extension = FileHandlerUtil.getExtensionType(file.getName());

        if (extension.equalsIgnoreCase("gif")) {
            writeGifThumbnail(file, dest);
        } else if (FileHandlerUtil.isImageFile(file.getName())) {
            writeThumbnailFromImage(file, dest, extension);
        } else {
            writeThumbnailFromVideo(file, dest);
        }
    }

    public void writeThumbnailFromImage(File image, String dest, String extension) throws IOException {
        BufferedImage src = ImageIO.read(image);
        createDirectoryIfNotExists(dest);

        File thumbnailDestination = new File(dest + "/" + image.getName());

        //Writes thumbnail
        write(getThumbnailImage(src, extension), extension, thumbnailDestination);
    }

    public void write(BufferedImage src, String imageFormat, File destination) throws IOException {
        ImageIO.write(src, imageFormat, destination);
    }

    public BufferedImage getThumbnailImage(BufferedImage original, String extension) {
        int type = extension.equalsIgnoreCase("png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

        BufferedImage thumbNail = new BufferedImage(IMAGE_RESIZE_SIZE, IMAGE_RESIZE_SIZE, type);
        Graphics2D g = thumbNail.createGraphics();
        g.drawImage(original, 0, 0, IMAGE_RESIZE_SIZE, IMAGE_RESIZE_SIZE, null);
        g.dispose();

        return thumbNail;
    }

    public void writeGifThumbnail(File gifFile, String dest) {
        FFmpegBuilder builder = new FFmpegBuilder();
        builder.setInput(gifFile.getAbsolutePath());
        builder.setComplexFilter(String.format("[0:v] scale=%s:%s:flags=lanczos,split [a][b]; [a] palettegen=reserve_transparent=on:transparency_color=ffffff [p]; [b][p] paletteuse", IMAGE_RESIZE_SIZE, IMAGE_RESIZE_SIZE));
        builder.addOutput(dest + gifFile.getName()).done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

    public void writeThumbnailFromVideo(File video, String dest) throws IOException {
        createDirectoryIfNotExists(dest);

        FFmpegBuilder builder = new FFmpegBuilder();
        builder.setInput(video.getAbsolutePath());
        builder.addOutput(dest + video.getName() + ".png")
            .setFrames(1)
            .setVideoFilter(String.format("select='gte(n\\,10)',scale=%s:%s", IMAGE_RESIZE_SIZE, IMAGE_RESIZE_SIZE))
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

    public void createDirectoryIfNotExists(String dest) throws IOException {
        File destFolder = new File(dest);

        if (!destFolder.exists()) {
            Files.createDirectory(destFolder.toPath());
        }
    }
}
