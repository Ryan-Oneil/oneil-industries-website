import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import Media from "../../components/Gallery/Media";
import "../../assets/css/layout.css";
import EditMediaForm from "../../components/formElements/EditMediaForm";
import RenderMedias from "../../components/Gallery/RenderMedias";
import { forceCheck } from "react-lazyload";
import { Button, Modal, Row } from "antd";
import {
  deleteMedia,
  fetchAlbums,
  fetchImages
} from "../../reducers/mediaReducer";
import { BASE_URL } from "../../apis/api";

export default props => {
  const { medias } = useSelector(state => state.medias.entities);
  const [mediaId, setMediaId] = useState("");
  const [activeMedia, setActiveMedia] = useState("");
  const [mediaIds, setMediaIds] = useState([]);
  const dispatch = useDispatch();
  const mediaList = mediaIds.map(id => medias[id]);
  const { name } = props.user;

  const handleShowDialog = mediaID => {
    setMediaId(mediaID);
  };

  useEffect(() => {
    setActiveMedia(medias[mediaId]);
  }, [mediaId]);

  useEffect(() => {
    forceCheck();
    dispatch(fetchImages(`/gallery/medias/user/${name}`)).then(mediaIds =>
      setMediaIds(mediaIds)
    );
    dispatch(fetchAlbums(`/gallery/myalbums/${name}`));
  }, []);

  return (
    <div className="marginPadding">
      <Row justify="center" gutter={[32, 32]}>
        {RenderMedias(mediaList, handleShowDialog, false)}
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
      {/*{this.state.isOpen && (*/}
      {/*  <Modal title={activeMedia.name} closeModal={handleShowDialog}>*/}
      {/*    <div className="image">*/}
      {/*      <a*/}
      {/*        href={`${BASE_URL}/gallery/${activeMedia.mediaType}/${activeMedia.fileName}`}*/}
      {/*      >*/}
      {/*        <Media*/}
      {/*          media={activeMedia}*/}
      {/*          renderVideoControls={true}*/}
      {/*          fullSize={true}*/}
      {/*        />*/}
      {/*      </a>*/}
      {/*    </div>*/}
      {/*    <button*/}
      {/*      value="Delete"*/}
      {/*      className="centerButton ui negative button center aligned bottomMargin"*/}
      {/*      onClick={() => {*/}
      {/*        this.props.deleteMedia(*/}
      {/*          `/gallery/media/delete/${activeMedia.id}`,*/}
      {/*          activeMedia.id*/}
      {/*        );*/}
      {/*        this.setState({ isOpen: false });*/}
      {/*      }}*/}
      {/*    >*/}
      {/*      Delete*/}
      {/*    </button>*/}
      {/*    <EditMediaForm*/}
      {/*      media={activeMedia}*/}
      {/*      initialValues={{*/}
      {/*        name: activeMedia.name,*/}
      {/*        privacy: activeMedia.linkStatus*/}
      {/*      }}*/}
      {/*    />*/}
      {/*    {deleteError && renderErrorMessage(deleteError)}*/}
      {/*  </Modal>*/}
      {/*)}*/}
    </div>
  );
};
