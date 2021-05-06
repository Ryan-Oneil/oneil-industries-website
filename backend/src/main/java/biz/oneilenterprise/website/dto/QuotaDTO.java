package biz.oneilenterprise.website.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class QuotaDTO {

    private long used;

    @NotNull
    @Min(value = 0, message = "Max quota must be a positive number")
    private int max;

    @NotNull
    private boolean ignoreQuota;

    public QuotaDTO(long used, int max, boolean ignoreQuota) {
        this.used = used;
        this.max = max;
        this.ignoreQuota = ignoreQuota;
    }

    public QuotaDTO() {
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
