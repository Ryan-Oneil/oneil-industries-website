package biz.oneilindustries.website.validation;

import java.io.File;
import java.util.Optional;
import javax.validation.constraints.NotNull;

public class GalleryUpload {

    private File file;

    @NotNull
    private String name;

    private String privacy = "unlisted";

    private String album;

    private Boolean showUnlistedImages;

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

    public Optional<String> getAlbum() {
        return Optional.ofNullable(album);
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Boolean getShowUnlistedImages() {
        return showUnlistedImages;
    }

    public void setShowUnlistedImages(Boolean showUnlistedImages) {
        this.showUnlistedImages = showUnlistedImages;
    }
}
