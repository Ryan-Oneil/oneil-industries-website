package biz.oneilindsutries.website.gallery;

import biz.oneilindsutries.website.entity.Album;
import biz.oneilindsutries.website.entity.Media;

import java.util.List;

public class MediaAlbum {

    private Album album;

    private List<Media> media;

    public MediaAlbum(Album album, List<Media> media) {
        this.album = album;
        this.media = media;
    }

    public void addMediaToAlbum(Media media) {
        if (media == null) return;
        this.media.add(media);
    }

    public void removeMediaFromAlbum(Media media) {
        if (media == null) return;
        this.media.remove(media);
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }
}
