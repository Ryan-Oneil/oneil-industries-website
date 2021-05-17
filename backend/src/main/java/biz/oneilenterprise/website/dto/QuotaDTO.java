package biz.oneilenterprise.website.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class QuotaDTO {

    private Long used;

    @NotNull(message = "Missing max value")
    @Min(value = 0, message = "Max quota must be a positive number")
    private Integer max;

    @NotNull(message = "Missing ignore quota value")
    private Boolean ignoreQuota;

    public QuotaDTO(long used, int max, boolean ignoreQuota) {
        this.used = used;
        this.max = max;
        this.ignoreQuota = ignoreQuota;
    }

    public QuotaDTO() {
    }

    public Long getUsed() {
        return used;
    }

    public Integer getMax() {
        return max;
    }

    public Boolean isIgnoreQuota() {
        return ignoreQuota;
    }

    @Override
    public String toString() {
        return "QuotaDTO{" +
            "used=" + used +
            ", max=" + max +
            ", ignoreQuota=" + ignoreQuota +
            '}';
    }
}
