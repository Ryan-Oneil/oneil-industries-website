package biz.oneilenterprise.website.validation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ShareLinkForm {

    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "A expiry date is required yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})Z$", message = "Expiry date must follow UTC format yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String expires;

    public ShareLinkForm(String title, String expires) {
        this.title = title;
        this.expires = expires;
    }

    public ShareLinkForm() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return "ShareLinkForm{" +
            "title='" + title + '\'' +
            ", expires=" + expires +
            '}';
    }
}
