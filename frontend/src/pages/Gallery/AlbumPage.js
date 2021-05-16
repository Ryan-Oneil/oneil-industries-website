import React, { useEffect, useState } from "react";
import { fetchAlbumWithImages } from "../../reducers/mediaReducer";
import { Col, Empty, Row } from "antd";
import MediaModal from "../../components/Gallery/MediaModal";
import MediaCard from "../../components/Gallery/MediaCard";

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

  const renderMedias = () => {
    return album.medias.map(media => {
      return (
        <Col xs={24} sm={12} md={12} lg={8} xl={6} xxl={4} key={media.id}>
          <MediaCard
            mediaFileName={media.fileName}
            mediaType={media.mediaType}
            dateAdded={media.dateAdded}
            handleShowDialog={handleShowDialog.bind(this, media)}
          />
        </Col>
      );
    });
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
        {renderMedias()}
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
