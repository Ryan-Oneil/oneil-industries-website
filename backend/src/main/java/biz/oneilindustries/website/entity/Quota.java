package biz.oneilindustries.website.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Quota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    //Storage space used in KBs
    private long used;

    //Max allowed storage quota in GB
    private int max = 25;

    @Column(name = "ignore_quota")
    private boolean ignoreQuota;

    public Quota() {
    }

    public Quota(String username, long used, int max, boolean ignoreQuota) {
        this.username = username;
        this.used = used;
        this.max = max;
        this.ignoreQuota = ignoreQuota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
