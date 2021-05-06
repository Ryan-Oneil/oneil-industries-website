package biz.oneilenterprise.website.dto;

import javax.validation.constraints.NotNull;

public class GalleryUploadDTO {

    @NotNull
    private String name;

    private String privacy = "unlisted";

    private String albumId;

    public GalleryUploadDTO() {
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
