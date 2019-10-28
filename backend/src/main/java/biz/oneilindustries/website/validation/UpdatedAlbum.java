package biz.oneilindustries.website.validation;

public class UpdatedAlbum {
    private String name;
    private boolean showUnlistedImages;

    public UpdatedAlbum() {
    }

    public UpdatedAlbum(String name, boolean showUnlistedImages) {
        this.name = name;
        this.showUnlistedImages = showUnlistedImages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowUnlistedImages() {
        return showUnlistedImages;
    }

    public void setShowUnlistedImages(boolean showUnlistedImages) {
        this.showUnlistedImages = showUnlistedImages;
    }
}
