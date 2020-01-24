package biz.oneilindustries.website.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "album")
public class Album {

    @Id
    private String id;

    private String name;

    private String creator;

    @Column(name = "show_unlisted_images")
    private boolean showUnlistedImages;

    @OneToMany(mappedBy = "album")
    private List<Media> medias;

    public Album(String id, String name, String creator, boolean showUnlistedImages) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.showUnlistedImages = showUnlistedImages;
    }

    public Album() {
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

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", showUnlistedImages=" + showUnlistedImages +
                '}';
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
}
