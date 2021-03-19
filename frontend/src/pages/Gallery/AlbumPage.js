import React, { useEffect, useState } from "react";
import RenderMedias from "../../components/Gallery/RenderMedias";
import { fetchAlbumWithImages } from "../../reducers/mediaReducer";
import { Empty, Row } from "antd";
import MediaModal from "../../components/Gallery/MediaModal";

export default props => {
  const {
    match: { params }
  } = props;
  const [album, setAlbum] = useState({ medias: [] });
  const [loading, setLoading] = useState(true);
  const [activeMedia, setActiveMedia] = useState("");

  useEffect(() => {
    fetchAlbumWithImages(`/gallery/album/${params.albumName}`)
      .then(album => setAlbum(album))
      .finally(() => setLoading(false));
  }, []);

  const handleShowDialog = media => {
    setActiveMedia(media);
  };

  return (
    <div className={"topPadding"}>
      <h1 className="centerText bigText whiteText">{album.name}</h1>
      {album.medias.length === 0 && (
        <Empty
          description={`${loading ? "Loading Album" : "No album found"}`}
          style={{ color: "white" }}
        />
      )}
      <Row gutter={[32, 32]} justify="center">
        {RenderMedias(album.medias, handleShowDialog)}
      </Row>
      {activeMedia && (
        <MediaModal
          activeMedia={activeMedia}
          extraMediaInfo={<h1>{activeMedia.uploader}</h1>}
          closeModalAction={() => setActiveMedia("")}
          showMediaPreview
        />
      )}
    </div>
  );
};
