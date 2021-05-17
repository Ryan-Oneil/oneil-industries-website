package biz.oneilenterprise.website.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "link_view")
public class LinkView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ip;

    @ManyToOne(targetEntity =Link.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "link_id")
    private Link link;

    @Column(name = "date_time")
    private Date dateTime;

    public LinkView(String ip, Link link) {
        this.ip = ip;
        this.link = link;
        this.dateTime = new Date();
    }

    public LinkView() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
