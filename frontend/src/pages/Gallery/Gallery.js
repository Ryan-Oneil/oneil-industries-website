import React, { useEffect, useState } from "react";
import RenderMedias from "../../components/Gallery/RenderMedias";
import { BASE_URL } from "../../apis/api";
import Media from "../../components/Gallery/Media";
import { useDispatch, useSelector } from "react-redux";
import { forceCheck } from "react-lazyload";
import { Modal, Row } from "antd";
import { fetchImages } from "../../reducers/mediaReducer";

export default () => {
  const [mediaId, setMediaId] = useState("");
  const [activeMedia, setActiveMedia] = useState("");
  const [mediaIds, setMediaIds] = useState([]);
  const dispatch = useDispatch();
  const { medias } = useSelector(state => state.medias.entities);
  const mediaList = mediaIds.map(id => medias[id]);

  const handleShowDialog = mediaID => {
    setMediaId(mediaID);
  };

  useEffect(() => {
    dispatch(fetchImages("/gallery/medias")).then(mediaIds =>
      setMediaIds(mediaIds)
    );
  }, []);

  useEffect(() => {
    setActiveMedia(medias[mediaId]);
  }, [mediaId]);

  // componentDidUpdate(prevProps, prevState, snapshot) {
  //   //Fixes a lazy loading bug that prevents a image from being lazy loaded when it was previously created in another page
  //   forceCheck();
  // }

  return (
    <>
      <Row justify="center" gutter={[32, 32]}>
        {RenderMedias(mediaList, handleShowDialog, true)}
      </Row>
      {activeMedia && (
        <Modal
          title={activeMedia.name}
          visible={mediaId}
          onCancel={() => setMediaId("")}
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
