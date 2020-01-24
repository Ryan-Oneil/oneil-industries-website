package biz.oneilindustries.website.pojo;

public class AlbumDetails {

    private String id;
    private String name;
    private boolean showUnlistedImages;

    public AlbumDetails(String id, String name, boolean showUnlistedImages) {
        this.id = id;
        this.name = name;
        this.showUnlistedImages = showUnlistedImages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getShowUnlistedImages() {
        return showUnlistedImages;
    }

    public void setShowUnlistedImages(boolean showUnlistedImages) {
        this.showUnlistedImages = showUnlistedImages;
    }
}
