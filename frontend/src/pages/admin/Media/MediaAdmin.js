import React, { useEffect, useState } from "react";
import { BASE_URL } from "../../../apis/api";
import EditMediaForm from "../../../components/formElements/EditMediaForm";
import { ADMIN_GET_MEDIAS_ENDPOINT } from "../../../apis/endpoints";
import MediaGrid from "../../../components/Gallery/MediaGrid";
import { Button, Modal } from "antd";
import { deleteMedia } from "../../../reducers/mediaReducer";
import { useDispatch, useSelector } from "react-redux";
import Media from "../../../components/Gallery/Media";

export default () => {
  const dispatch = useDispatch();
  const { medias } = useSelector(state => state.medias.entities);
  const [mediaId, setMediaId] = useState("");
  const [activeMedia, setActiveMedia] = useState("");

  const handleShowDialog = media => {
    setMediaId(media);
  };

  useEffect(() => {
    setActiveMedia(medias[mediaId]);
  }, [mediaId]);

  return (
    <div style={{ marginTop: "20px" }}>
      <MediaGrid
        imageEndpoint={`${ADMIN_GET_MEDIAS_ENDPOINT}image`}
        videoEndpoint={`${ADMIN_GET_MEDIAS_ENDPOINT}video`}
        handleShowDialog={handleShowDialog}
      />
      {activeMedia && (
        <Modal
          title={activeMedia.name}
          visible={mediaId}
          onCancel={() => setMediaId("")}
          footer={null}
          width={550}
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
          <Button
            value="Delete"
            className="centerButton"
            type="danger"
            onClick={() => {
              dispatch(
                deleteMedia(
                  `/gallery/media/delete/${activeMedia.id}`,
                  activeMedia.id
                )
              );
              setActiveMedia("");
            }}
          >
            Delete
          </Button>
          <EditMediaForm media={activeMedia} />
        </Modal>
      )}
    </div>
  );
};
