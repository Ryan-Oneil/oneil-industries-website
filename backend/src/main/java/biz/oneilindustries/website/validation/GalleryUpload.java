package biz.oneilindustries.website.validation;

import javax.validation.constraints.NotNull;

public class GalleryUpload {

    @NotNull
    private String name;

    private String privacy = "unlisted";

    private String albumId;

    public GalleryUpload() {
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

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
