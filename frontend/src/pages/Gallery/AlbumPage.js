import React, { useEffect, useState } from "react";
import Media from "../../components/Gallery/Media";
import RenderMedias from "../../components/Gallery/RenderMedias";
import { BASE_URL } from "../../apis/api";
import { fetchAlbumWithImages } from "../../reducers/mediaReducer";
import { Modal, Row } from "antd";

export default props => {
  const {
    match: { params }
  } = props;
  const [album, setAlbum] = useState({ medias: [] });
  const [activeMedia, setActiveMedia] = useState("");

  useEffect(() => {
    fetchAlbumWithImages(`/gallery/album/${params.albumName}`).then(album =>
      setAlbum(album)
    );
  }, []);

  const handleShowDialog = media => {
    setActiveMedia(media);
  };

  return (
    <div style={{ marginTop: "20px" }}>
      <Row gutter={[32, 32]} justify="center">
        {RenderMedias(album.medias, handleShowDialog, true)}
      </Row>
      {activeMedia && (
        <Modal
          title={activeMedia.name}
          visible={activeMedia}
          onCancel={() => setActiveMedia("")}
          footer={null}
        >
          <a
            href={`${BASE_URL}/gallery/${activeMedia.mediaType}/${activeMedia.fileName}`}
          >
            <Media
              media={activeMedia}
              renderVideoControls={true}
              fullSize={true}
            />
          </a>
          <div className="centerText">
            <p>Uploader: {activeMedia.uploader}</p>
            <p>Uploaded: {activeMedia.dateAdded}</p>
          </div>
        </Modal>
      )}
    </div>
  );
};
