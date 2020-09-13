import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Media from "../../../components/Gallery/Media";
import "../../../assets/css/layout.css";
import { Button, Modal } from "antd";
import MediaGrid from "../../../components/Gallery/MediaGrid";
import { USER_MEDIAS_ENDPOINT } from "../../../apis/endpoints";
import { BASE_URL } from "../../../apis/api";
import { deleteMedia } from "../../../reducers/mediaReducer";
import EditMediaForm from "../../../components/formElements/EditMediaForm";

export default props => {
  const { medias } = useSelector(state => state.medias.entities);
  const [mediaId, setMediaId] = useState("");
  const [activeMedia, setActiveMedia] = useState("");
  const dispatch = useDispatch();
  const { name } = props.user;

  const handleShowDialog = mediaID => {
    setMediaId(mediaID);
  };

  useEffect(() => {
    setActiveMedia(medias[mediaId]);
  }, [mediaId]);

  return (
    <div style={{ height: "100%", overflow: "auto" }}>
      <MediaGrid
        imageEndpoint={`${USER_MEDIAS_ENDPOINT}${name}/image`}
        videoEndpoint={`${USER_MEDIAS_ENDPOINT}${name}/video`}
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
          <EditMediaForm
            media={activeMedia}
            initialValues={{
              name: activeMedia.name,
              privacy: activeMedia.linkStatus
            }}
          />
        </Modal>
      )}
    </div>
  );
};
