package biz.oneilindsutries.gallery.demo.entity;

import javax.persistence.*;

@Entity
public class Image {

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

    public Image(String name, String fileName, String linkStatus, String uploader, String dateAdded) {
        this.name = name;
        this.fileName = fileName;
        this.linkStatus = linkStatus;
        this.uploader = uploader;
        this.dateAdded = dateAdded;
    }

    public Image() {
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

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + fileName + '\'' +
                ", linkStatus='" + linkStatus + '\'' +
                ", uploader='" + uploader + '\'' +
                '}';
    }
}
