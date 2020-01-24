package biz.oneilindustries.website.validation;

import java.io.File;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GalleryUpload {

    private File file;

    @NotNull
    private String name;

    private String privacy = "unlisted";

    private String albumName;

    private String newAlbum;

    private Boolean showUnlistedImages;

    public GalleryUpload(File file, @NotNull String name, String privacy, String albumName, String newAlbum, Boolean showUnlistedImages) {
        this.file = file;
        this.name = name;
        this.privacy = privacy;
        this.albumName = albumName;
        this.newAlbum = newAlbum;
        this.showUnlistedImages = showUnlistedImages;
    }

    public GalleryUpload(File file, @NotNull String name, String privacy, String albumName) {
        this.file = file;
        this.name = name;
        this.privacy = privacy;
        this.albumName = albumName;
    }

    public GalleryUpload(@NotNull String name, String privacy,
        @Size(min = 1, message = "Please enter an album names") String albumName) {
        this.name = name;
        this.privacy = privacy;
        this.albumName = albumName;
    }

    public GalleryUpload() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
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

    public String getNewAlbum() {
        return newAlbum;
    }

    public void setNewAlbum(String newAlbum) {
        this.newAlbum = newAlbum;
    }

    public Boolean getShowUnlistedImages() {
        return showUnlistedImages;
    }

    public void setShowUnlistedImages(Boolean showUnlistedImages) {
        this.showUnlistedImages = showUnlistedImages;
    }
}
