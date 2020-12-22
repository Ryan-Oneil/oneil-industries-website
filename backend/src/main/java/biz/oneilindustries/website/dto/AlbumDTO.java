package biz.oneilindustries.website.dto;

import java.util.List;

public class AlbumDTO {

    private String id;
    private String name;
    private String creator;
    private List<MediaDTO> medias;

    public AlbumDTO() {
    }

    public AlbumDTO(String id, String name, String creator, List<MediaDTO> medias) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.medias = medias;
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

    public List<MediaDTO> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaDTO> medias) {
        this.medias = medias;
    }
}
