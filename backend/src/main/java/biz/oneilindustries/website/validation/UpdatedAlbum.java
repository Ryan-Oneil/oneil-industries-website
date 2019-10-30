package biz.oneilindustries.website.validation;

public class UpdatedAlbum {
    private String newAlbumName;
    private boolean showUnlistedImages;

    public UpdatedAlbum() {
    }

    public UpdatedAlbum(String newAlbumName, boolean showUnlistedImages) {
        this.newAlbumName = newAlbumName;
        this.showUnlistedImages = showUnlistedImages;
    }

    public String getNewAlbumName() {
        return newAlbumName;
    }

    public void setNewAlbumName(String newAlbumName) {
        this.newAlbumName = newAlbumName;
    }

    public boolean isShowUnlistedImages() {
        return showUnlistedImages;
    }

    public void setShowUnlistedImages(boolean showUnlistedImages) {
        this.showUnlistedImages = showUnlistedImages;
    }
}
