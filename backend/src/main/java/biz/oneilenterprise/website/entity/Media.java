package biz.oneilenterprise.website.entity;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "link_status")
    private String linkStatus;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "uploader", referencedColumnName = "username")
    private User uploader;

    @Column(name = "date_added")
    private String dateAdded;

    @ManyToOne
    @JoinColumn(name="album_id")
    private Album album;

    @Column(name = "media_type")
    private String mediaType;

    private Long size;

    @OneToOne(mappedBy = "media", cascade = CascadeType.REMOVE)
    private PublicMediaApproval publicMediaApproval;

    public Media(String name, String fileName, String linkStatus, User uploader, String dateAdded,
        Album album, String mediaType, Long size) {
        this.name = name;
        this.fileName = fileName;
        this.linkStatus = linkStatus;
        this.uploader = uploader;
        this.dateAdded = dateAdded;
        this.album = album;
        this.mediaType = mediaType;
        this.size = size;
    }

    public Media(String name, String fileName, String linkStatus, User uploader, String dateAdded, Long size) {
        this.name = name;
        this.fileName = fileName;
        this.linkStatus = linkStatus;
        this.uploader = uploader;
        this.dateAdded = dateAdded;
        this.size = size;
    }

    public Media(int mediaID) {
        this.id = mediaID;
    }

    public Media() {
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

    public void setFileName(String path) {
        this.fileName = path;
    }

    public String getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(String linkStatus) {
        this.linkStatus = linkStatus;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
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

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", linkStatus='" + linkStatus + '\'' +
                ", uploader='" + uploader + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", media='" + mediaType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return getId() == media.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public PublicMediaApproval getPublicMediaApproval() {
        return publicMediaApproval;
    }

    public void setPublicMediaApproval(PublicMediaApproval publicMediaApproval) {
        this.publicMediaApproval = publicMediaApproval;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
