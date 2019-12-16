package biz.oneilindustries.website.validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdatedQuota {

    @NotNull
    @Min(value = 0, message = "Max quota must be a positive number")
    private int max;

    @NotNull
    private boolean ignoreQuota;

    public UpdatedQuota(int max, boolean ignoreQuota) {
        this.max = max;
        this.ignoreQuota = ignoreQuota;
    }

    public UpdatedQuota() {
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
