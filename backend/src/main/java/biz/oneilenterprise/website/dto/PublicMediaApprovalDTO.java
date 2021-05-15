package biz.oneilenterprise.website.dto;

public class PublicMediaApprovalDTO {

    private int mediaId;
    private String publicName;
    private String status;
    private MediaDTO media;

    public PublicMediaApprovalDTO() {
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MediaDTO getMedia() {
        return media;
    }

    public void setMedia(MediaDTO media) {
        this.media = media;
    }
}
