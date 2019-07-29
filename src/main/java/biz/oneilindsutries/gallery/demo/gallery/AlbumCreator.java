package biz.oneilindsutries.gallery.demo.gallery;

import biz.oneilindsutries.gallery.demo.entity.Album;
import biz.oneilindsutries.gallery.demo.entity.Image;
import biz.oneilindsutries.gallery.demo.service.ImageService;

import java.util.ArrayList;
import java.util.List;

public class AlbumCreator {

    private final ImageService imageService;

    public AlbumCreator(ImageService imageService) {
        this.imageService = imageService;
    }

    public ImageAlbum createAlbum(Album album) {
        if (album == null) return null;

        List<Image> images = imageService.getAlbumImages(album.getId());

        if (images == null) images = new ArrayList<>();

        return new ImageAlbum(album,images);
    }
}
