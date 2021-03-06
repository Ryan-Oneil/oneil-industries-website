package biz.oneilenterprise.website.dto;

import javax.validation.constraints.NotEmpty;

public class MediaUploadDTO {

    @NotEmpty
    private String name;

    private String albumId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
