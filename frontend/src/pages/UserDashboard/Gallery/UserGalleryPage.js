import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Media from "../../../components/Gallery/Media";
import "../../../assets/css/layout.css";
import { Button, Modal } from "antd";
import { BASE_URL } from "../../../apis/api";
import { deleteMedia, fetchAlbums } from "../../../reducers/mediaReducer";
import EditMediaForm from "../../../components/formElements/EditMediaForm";
import ManageMediaGrid from "../../../components/Gallery/ManageMediaGrid";

export default () => {
  const { medias, albums } = useSelector(state => state.medias.entities);
  const [activeMedia, setActiveMedia] = useState("");
  const dispatch = useDispatch();
  const { name } = useSelector(state => state.auth.user);

  const handleShowDialog = media => {
    setActiveMedia(media);
  };

  useEffect(() => {
    dispatch(fetchAlbums(`/gallery/myalbums/${name}`));
  }, []);

  useEffect(() => {
    if (activeMedia) {
      setActiveMedia(medias[activeMedia.id]);
    }
  }, [medias]);

  return (
    <div>
      <ManageMediaGrid
        handleShowDialog={handleShowDialog}
        albums={Object.values(albums)}
      />
      {activeMedia && (
        <Modal
          title={activeMedia.name}
          visible={activeMedia}
          onCancel={() => setActiveMedia("")}
          footer={null}
          width={550}
          maskStyle={{ backgroundColor: "rgba(0, 0, 0, 0.7)" }}
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
