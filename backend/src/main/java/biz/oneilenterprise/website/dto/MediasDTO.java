package biz.oneilenterprise.website.dto;

import javax.validation.constraints.NotNull;

public class MediasDTO {

    @NotNull
    private Integer[] mediaIds;

    private String linkStatus;

    public MediasDTO() {
    }

    public Integer[] getMediaIds() {
        return mediaIds;
    }

    public void setMediaIds(Integer[] mediaIds) {
        this.mediaIds = mediaIds;
    }

    public String getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(String linkStatus) {
        this.linkStatus = linkStatus;
    }
}
