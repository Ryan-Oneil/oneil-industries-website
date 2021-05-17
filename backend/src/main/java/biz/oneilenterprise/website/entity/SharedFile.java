package biz.oneilenterprise.website.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file")
public class SharedFile {

    @Id
    private String id;

    private String name;

    private long size;

    @ManyToOne(targetEntity = Link.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "link_id")
    private Link link;

    public SharedFile() {
    }

    public SharedFile(String id, String name, long size, Link link) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
