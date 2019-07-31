package biz.oneilindsutries.website.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GalleryUpload {

    private MultipartFile file;

    @NotNull
    private String name;

    @NotNull
    private String privacy;

    @NotNull
    @Size(min = 1,message = "Please enter an album names")
    private String albumName;

    private String newalbumName;

    private Boolean showUnlistedImages;

    public GalleryUpload(MultipartFile file, @NotNull String name, @NotNull String privacy,@NotNull String albumName, String newalbumName, Boolean showUnlistedImages) {
        this.file = file;
        this.name = name;
        this.privacy = privacy;
        this.albumName = albumName;
        this.newalbumName = newalbumName;
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

    public String getNewalbumName() {
        return newalbumName;
    }

    public void setNewalbumName(String newalbumName) {
        this.newalbumName = newalbumName;
    }

    public Boolean getShowUnlistedImages() {
        return showUnlistedImages;
    }

    public void setShowUnlistedImages(Boolean showUnlistedImages) {
        this.showUnlistedImages = showUnlistedImages;
    }
}
