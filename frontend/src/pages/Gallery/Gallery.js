import React, { useState } from "react";
import { Button } from "antd";
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

  const handleShowDialog = media => {
    setActiveMedia(media);
  };

  const handleCloseDialog = () => {
    setActiveMedia("");
  };

  return (
    <div style={{ paddingLeft: 24, paddingRight: 24 }}>
      <h1 className={"bigText centerText whiteColor removeMargin"}>Public</h1>
      <Button
        className={"lightGreenBackground roundedBorder"}
        style={{ color: "white", marginBottom: "1%" }}
        icon={<UploadOutlined />}
        size={"large"}
        onClick={uploadButtonClick}
      >
        Upload
      </Button>
      <MediaGrid
        height={"80vh"}
        mediaEndpoint={PUBLIC_MEDIAS_ENDPOINT}
        mediaCardLayout={item => (
          <MediaCard
            mediaFileName={item.fileName}
            title={item.name}
            mediaType={item.mediaType}
            dateAdded={item.dateAdded}
            handleShowDialog={handleShowDialog.bind(this, item)}
          />
        )}
      />
      {activeMedia && (
        <MediaModal
          activeMedia={activeMedia}
          extraMediaInfo={<h1>{activeMedia.uploader}</h1>}
          closeModalAction={handleCloseDialog}
          showMediaPreview
        />
      )}
    </div>
  );
};
