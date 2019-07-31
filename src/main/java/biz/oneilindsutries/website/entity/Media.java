package biz.oneilindsutries.website.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "link_status")
    private String linkStatus;

    private String uploader;

    @Column(name = "date_added")
    private String dateAdded;

    @Column(name = "album_id")
    private Integer albumID;

    @Column(name = "media_type")
    private String mediaType;

    public Media(String name, String fileName, String linkStatus, String uploader, String dateAdded, Integer albumID, String mediaType) {
        this.name = name;
        this.fileName = fileName;
        this.linkStatus = linkStatus;
        this.uploader = uploader;
        this.dateAdded = dateAdded;
        this.albumID = albumID;
        this.mediaType = mediaType;
    }

    public Media(String name, String fileName, String linkStatus, String uploader, String dateAdded) {
        this.name = name;
        this.fileName = fileName;
        this.linkStatus = linkStatus;
        this.uploader = uploader;
        this.dateAdded = dateAdded;
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

    public Integer getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
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
                ", albumID=" + albumID +
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
}
