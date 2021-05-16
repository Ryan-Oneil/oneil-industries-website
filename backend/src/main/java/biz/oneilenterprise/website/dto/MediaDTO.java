package biz.oneilenterprise.website.dto;

public class MediaDTO {

    private int id;
    private String name;
    private String fileName;
    private String linkStatus;
    private String uploader;
    private String dateAdded;
    private String mediaType;
    private Long size;
    private String url;
    private PublicMediaApprovalDTO publicMediaApproval;

    public MediaDTO() {
    }

    public MediaDTO(int id, String name, String fileName, String linkStatus, String uploader, String dateAdded, String mediaType, Long size,
        String url) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
        this.linkStatus = linkStatus;
        this.uploader = uploader;
        this.dateAdded = dateAdded;
        this.mediaType = mediaType;
        this.size = size;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(String linkStatus) {
        this.linkStatus = linkStatus;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public PublicMediaApprovalDTO getPublicMediaApproval() {
        return publicMediaApproval;
    }

    public void setPublicMediaApproval(PublicMediaApprovalDTO publicMediaApproval) {
        this.publicMediaApproval = publicMediaApproval;
    }
}
