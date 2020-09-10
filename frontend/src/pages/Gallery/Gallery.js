import React, { useState } from "react";
import { BASE_URL } from "../../apis/api";
import Media from "../../components/Gallery/Media";
import { Modal } from "antd";
import MediaGrid from "../../components/Gallery/MediaGrid";
import { PUBLIC_MEDIAS_ENDPOINT } from "../../apis/endpoints";

export default () => {
  const [activeMedia, setActiveMedia] = useState("");

  const handleShowDialog = mediaID => {
    setActiveMedia(mediaID);
  };

  return (
    <>
      <MediaGrid
        endpoint={PUBLIC_MEDIAS_ENDPOINT}
        handleShowDialog={handleShowDialog}
      />
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
    </>
  );
};
