package biz.oneilindsutries.gallery.demo.gallery;

import biz.oneilindsutries.gallery.demo.entity.Album;
import biz.oneilindsutries.gallery.demo.entity.Image;

import java.util.List;

public class ImageAlbum {

    private Album album;

    private List<Image> images;

    public ImageAlbum(Album album, List<Image> images) {
        this.album = album;
        this.images = images;
    }

    public void addImageToAlbum(Image image) {
        if (image == null) return;
        this.images.add(image);
    }

    public void removeImageFromAlbum(Image image) {
        if (image == null) return;
        this.images.remove(image);
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
