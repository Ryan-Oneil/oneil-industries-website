package biz.oneilindsutries.gallery.demo.entity;

import javax.persistence.*;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String creator;

    @Column(name = "show_unlisted_images")
    private boolean showUnlistedImages;

    public Album(String name, String creator, boolean showUnlistedImages) {
        this.name = name;
        this.creator = creator;
        this.showUnlistedImages = showUnlistedImages;
    }

    public Album() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isShowUnlistedImages() {
        return showUnlistedImages;
    }

    public void setShowUnlistedImages(boolean showUnlistedImages) {
        this.showUnlistedImages = showUnlistedImages;
    }
}
