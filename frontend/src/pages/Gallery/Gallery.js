import React, { useState } from "react";
import { Button, Modal } from "antd";
import MediaGrid from "../../components/Gallery/MediaGrid";
import { PUBLIC_MEDIAS_ENDPOINT } from "../../apis/endpoints";
import MediaCard from "../../components/Gallery/MediaCard";
import UploadOutlined from "@ant-design/icons/lib/icons/UploadOutlined";
import MediaModal from "../../components/Gallery/MediaModal";
import { useHistory } from "react-router-dom";
import { DASHBOARD, GALLERY_UPLOAD_URL } from "../../constants/constants";

export default () => {
  const [activeMedia, setActiveMedia] = useState("");
  let history = useHistory();

  const uploadButtonClick = () => {
    history.push(DASHBOARD + GALLERY_UPLOAD_URL);
  };

  const handleShowDialog = mediaID => {
    setActiveMedia(mediaID);
  };

  const handleCloseDialog = () => {
    setActiveMedia("");
  };

  return (
    <div style={{ marginTop: "24px" }}>
      <h1
        className={"bigText centerText whiteColor"}
        style={{ paddingLeft: 30 }}
      >
        Public
      </h1>

      <MediaGrid
        imageEndpoint={`${PUBLIC_MEDIAS_ENDPOINT}/image`}
        videoEndpoint={`${PUBLIC_MEDIAS_ENDPOINT}/video`}
        handleShowDialog={handleShowDialog}
        mediaCardLayout={item => (
          <MediaCard mediaItem={item} handleShowDialog={handleShowDialog} />
        )}
        tabExtraActions={{
          left: (
            <Button
              className={"uploadButton"}
              icon={<UploadOutlined />}
              size={"large"}
              onClick={uploadButtonClick}
            >
              Upload
            </Button>
          )
        }}
      />
      {activeMedia && (
        <Modal
          title={activeMedia.name}
          visible={activeMedia}
          onCancel={handleCloseDialog}
          footer={null}
          width={"80vw"}
          maskStyle={{ backgroundColor: "rgba(0, 0, 0, 0.7)" }}
          modalRender={() => {
            return (
              <MediaModal
                activeMedia={activeMedia}
                closeModalAction={handleCloseDialog}
                showMediaPreview
              />
            );
          }}
        />
      )}
    </div>
  );
};
