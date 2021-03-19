package biz.oneilenterprise.website.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feedback")
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

    private String subject;

    private String message;

    private Date time;

    @Column(name = "ip_address")
    private String ipAddress;

    public FeedBack(String name, String email, String subject, String message, String ipAddress) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.ipAddress = ipAddress;
        this.time = getCurrentTime();
    }

    public FeedBack() {
    }

    private Date getCurrentTime() {
        return new Date();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", subject='" + subject + '\'' +
            ", message='" + message + '\'' +
            ", time=" + time +
            ", ipAddress='" + ipAddress + '\'' +
            '}';
    }
}
