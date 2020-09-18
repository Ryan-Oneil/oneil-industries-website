package biz.oneilindustries.website.dto;

public class QuotaDTO {

    private long used;
    private int max;
    private boolean ignoreQuota;

    public QuotaDTO(long used, int max, boolean ignoreQuota) {
        this.used = used;
        this.max = max;
        this.ignoreQuota = ignoreQuota;
    }

    public long getUsed() {
        return used;
    }

    public int getMax() {
        return max;
    }

    public boolean isIgnoreQuota() {
        return ignoreQuota;
    }
}
