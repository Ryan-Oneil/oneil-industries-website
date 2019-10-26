package biz.oneilindustries.website.gallery;

import biz.oneilindustries.website.entity.Album;
import biz.oneilindustries.website.entity.Media;
import biz.oneilindustries.website.service.MediaService;

import java.util.ArrayList;
import java.util.List;

public class AlbumCreator {

    private final MediaService mediaService;

    public AlbumCreator(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public MediaAlbum createAlbum(Album album) {
        if (album == null) return null;

        List<Media> media = mediaService.getAlbumMedias(album.getId());

        if (media == null) media = new ArrayList<>();

        return new MediaAlbum(album, media);
    }
}
