import SelectWithDropDown from "../formElements/SelectWithDropDown";
import {
  addMediasToAlbum,
  deleteMedias,
  fetchAlbums,
  postNewAlbum,
  updateMediasLinkStatus,
} from "../../reducers/mediaReducer";
import { Col, message, Row, Select } from "antd";
import MediaModal from "./MediaModal";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getApiError } from "../../apis/ApiErrorHandler";
import UploadingMediaCard from "./UploadingMediaCard";

const MediaUploadedGrid = ({ files, onSelectAlbum, onDeleteMedia }) => {
  const dispatch = useDispatch();
  const [activeMedia, setActiveMedia] = useState("");
  const { name } = useSelector((state) => state.auth.user);
  const { albums } = useSelector((state) => state.medias.entities);
  const textStyle = { fontWeight: 600, padding: 10 };

  useEffect(() => {
    dispatch(fetchAlbums(`/gallery/myalbums/${name}`));
  }, []);

  const handleShowDialog = (media) => {
    setActiveMedia(media);
  };

  const changeUploadedMediasLinkStatus = (linkStatus) => {
    const mediaIds = files
      .filter((file) => file.uploadStatus === "complete")
      .map((media) => media.id);

    dispatch(updateMediasLinkStatus(mediaIds, linkStatus))
      .then((status) => {
        if (status) {
          message.info(status);
        } else {
          message.success(`Links status changed to ${linkStatus}`);
        }
      })
      .catch((error) => message.error(getApiError(error)));
  };

  const changeUploadedAlbum = (album) => {
    onSelectAlbum(album);
    const mediaIds = files
      .filter((file) => file.uploadStatus === "complete")
      .map((media) => media.id);

    dispatch(addMediasToAlbum(album, mediaIds)).then(() => {
      message.success("Successfully added selected medias to Album");
    });
  };

  const renderUploadingMedias = () => {
    return files.map((file) => {
      return (
        <Col xs={24} sm={12} md={12} lg={12} xl={8} xxl={4} key={file.uid}>
          <UploadingMediaCard
            progress={file.progress}
            uploadStatus={file.uploadStatus}
            url={file.url}
            deleteAction={() => {
              if (file.uploadStatus === "exception") {
                onDeleteMedia(file.uid);
                return;
              }
              dispatch(deleteMedias([file.id])).then(() => {
                onDeleteMedia(file.uid);
                message.success("Successfully deleted Media");
              });
            }}
            handleShowDialog={handleShowDialog.bind(this, file)}
          />
        </Col>
      );
    });
  };

  const selectStyle = { display: "inline-block", width: "50%" };
  return (
    <>
      {files.length > 0 && (
        <div
          style={{
            marginLeft: 32,
            marginTop: "2%",
            display: "flex",
          }}
        >
          <div
            style={{
              backgroundColor: "white",
              padding: 10,
              margin: "auto",
              width: "60%",
            }}
          >
            <div style={selectStyle}>
              <span style={textStyle}>Album</span>
              <SelectWithDropDown
                style={{ width: "70%" }}
                optionValues={Object.values(albums)}
                label={"Add medias to album"}
                onChange={(albumId) => changeUploadedAlbum(albumId)}
                onSubmit={(value) => dispatch(postNewAlbum(value))}
              />
            </div>

            <div style={selectStyle}>
              <span style={textStyle}>Privacy</span>
              <Select
                onSelect={(value) => changeUploadedMediasLinkStatus(value)}
                size="large"
                style={{ textAlign: "start", width: "70%" }}
                defaultValue={"unlisted"}
              >
                <Select.Option value="unlisted">Unlisted</Select.Option>
                <Select.Option value="public">Public</Select.Option>
              </Select>
            </div>
          </div>
        </div>
      )}
      <Row gutter={[64, 32]} type="flex" className={"topPadding"}>
        {renderUploadingMedias()}
      </Row>
      {activeMedia && (
        <MediaModal
          activeMedia={activeMedia}
          closeModalAction={(mediaDeleted) => {
            //Checks if the media was deleted in modal
            if (mediaDeleted === true) {
              onDeleteMedia(activeMedia.uid);
            }
            setActiveMedia("");
          }}
          showMediaPreview
          enableManagement
        />
      )}
    </>
  );
};
export default MediaUploadedGrid;
