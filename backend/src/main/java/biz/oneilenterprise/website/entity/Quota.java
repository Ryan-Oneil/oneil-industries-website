package biz.oneilenterprise.website.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "quota")
public class Quota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "quota")
    private User user;

    //Storage space used in KBs
    private long used;

    //Max allowed storage quota in GB
    private int max = 25;

    @Column(name = "ignore_quota")
    private boolean ignoreQuota;

    public Quota() {
    }

    public Quota(User user, long used) {
        this.user = user;
        this.used = used;
    }

    public Quota(User user, long used, int max, boolean ignoreQuota) {
        this.user = user;
        this.used = used;
        this.max = max;
        this.ignoreQuota = ignoreQuota;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isIgnoreQuota() {
        return ignoreQuota;
    }

    public void setIgnoreQuota(boolean ignoreQuota) {
        this.ignoreQuota = ignoreQuota;
    }

    public void increaseUsed(long amount) {
        this.used += amount;
    }

    public void decreaseUsed(long amount) {
        this.used -= amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
