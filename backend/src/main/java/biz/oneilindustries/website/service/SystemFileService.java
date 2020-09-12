package biz.oneilindustries.website.service;

import biz.oneilindustries.RandomIDGen;
import biz.oneilindustries.website.exception.ResourceNotFoundException;
import biz.oneilindustries.website.filecreater.FileHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
public class SystemFileService {

    private static final Logger logger = LogManager.getLogger(biz.oneilindustries.website.service.SystemFileService.class);

    public List<File> handleFileUpload(HttpServletRequest request, long uploadLimit, String destination, boolean generateRandomName)
        throws IOException, FileUploadException {
        ArrayList<File> uploadedFiles = new ArrayList<>();
        ServletFileUpload upload = new ServletFileUpload();
        upload.setSizeMax(uploadLimit);

        FileItemIterator iterator;
        //Gets the uploaded file from request
        try {
            iterator = upload.getItemIterator(request);
        } catch (IOException e) {
            throw new RuntimeException("No form data");
        }
        while(iterator.hasNext()) {
            FileItemStream item = iterator.next();

            if (item.isFormField()) continue;

            String fileName = generateRandomName ? RandomIDGen.getBase62(16) + "." + FileHandler.getExtensionType(item.getName()) :
                item.getName();

            File file = FileHandler.writeFile(item, fileName,  destination);

            uploadedFiles.add(file);
        }
        if (uploadedFiles.isEmpty()) throw new FileNotFoundException("No files uploaded");
        return uploadedFiles;
    }

    public void streamFolderAsZip(String folder, OutputStream dest) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(dest)) {
            Path pp = Paths.get(folder);
            Files.walk(pp)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    FileSystemResource resource = new FileSystemResource(path);
                    ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                    try {
                        zs.putNextEntry(zipEntry);
                        StreamUtils.copy(resource.getInputStream(), zs);
                        zs.closeEntry();
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                });
        }
    }

    public StreamingResponseBody streamFile(String fileLocation) {
        File fileToStream = new File(fileLocation);

        if (!fileToStream.exists()) {
            logger.error("File doesn't exist " + fileLocation);
            throw new ResourceNotFoundException("This file doesn't exist on the server");
        }
        return out -> Files.copy(fileToStream.toPath(), out);
    }
}
