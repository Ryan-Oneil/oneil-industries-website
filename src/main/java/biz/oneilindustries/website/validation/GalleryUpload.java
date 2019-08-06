package biz.oneilindustries.website.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GalleryUpload {

    private MultipartFile file;

    @NotNull
    private String name;

    private String privacy = "unlisted";

    @Size(min = 1, message = "Please enter an album names")
    private String albumName = "none";

    private String newAlbumName;

    private Boolean showUnlistedImages;

    public GalleryUpload(MultipartFile file, @NotNull String name, String privacy, String albumName, String newAlbumName, Boolean showUnlistedImages) {
        this.file = file;
        this.name = name;
        this.privacy = privacy;
        this.albumName = albumName;
        this.newAlbumName = newAlbumName;
        this.showUnlistedImages = showUnlistedImages;
    }

    public GalleryUpload() {
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getNewAlbumName() {
        return newAlbumName;
    }

    public void setNewAlbumName(String newAlbumName) {
        this.newAlbumName = newAlbumName;
    }

    public Boolean getShowUnlistedImages() {
        return showUnlistedImages;
    }

    public void setShowUnlistedImages(Boolean showUnlistedImages) {
        this.showUnlistedImages = showUnlistedImages;
    }
}
