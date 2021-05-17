import React, { useEffect, useState } from "react";
import { Col, Row, Select, message } from "antd";
import { getUploadProgress } from "../../../helpers";
import { useDispatch, useSelector } from "react-redux";
import {
  addMediasToAlbum,
  deleteMedia,
  fetchAlbums,
  postNewAlbum,
  updateMediasLinkStatus,
  uploadMedia
} from "../../../reducers/mediaReducer";
import SelectWithDropDown from "../../../components/formElements/SelectWithDropDown";
import UploadingMediaCard from "../../../components/Gallery/UploadingMediaCard";
import MediaModal from "../../../components/Gallery/MediaModal";
import Uploader from "../../../components/Uploader";
import PictureOutlined from "@ant-design/icons/lib/icons/PictureOutlined";
import { getApiError } from "../../../apis/ApiErrorHandler";
const { Option } = Select;

export default () => {
  const [files, setFiles] = useState([]);
  const [linkStatus, setLinkStatus] = useState("unlisted");
  const [selectedAlbumId, setSelectedAlbumId] = useState("");
  const [activeMedia, setActiveMedia] = useState("");
  const { name } = useSelector(state => state.auth.user);
  const { albums } = useSelector(state => state.medias.entities);
  const dispatch = useDispatch();
  const textStyle = { fontWeight: 600, padding: 10 };

  useEffect(() => {
    dispatch(fetchAlbums(`/gallery/myalbums/${name}`));
  }, []);

  const removeFile = fileId => {
    const oldFiles = [...files];

    setFiles(oldFiles.filter(file => file.uid !== fileId));
  };

  const updateMediaUploadProgress = mediaFile => {
    setFiles(prevState => {
      let updatedMedias = [...prevState];
      const removedMediaList = updatedMedias.filter(
        file => file.uid !== mediaFile.uid
      );
      return [...removedMediaList, mediaFile];
    });
  };

  const handleShowDialog = media => {
    setActiveMedia(media);
  };

  const onMediaSelected = file => {
    if (!file.type.includes("image") && !file.type.includes("video")) {
      message.error("This file format isn't supported");
      return;
    }
    file.progress = 0;
    file.url = "";
    file.id = "";
    file.uploadStatus = "active";

    setFiles(prevState => [...prevState, file]);
    dispatch(
      uploadMedia(
        "/gallery/upload",
        { linkStatus, albumId: selectedAlbumId },
        file,
        event => {
          file.progress = getUploadProgress(event);
          updateMediaUploadProgress(file);
        }
      )
    )
      .then(({ payload }) => {
        const media = payload.medias[0];

        file = {
          ...media,
          uploadStatus: "complete",
          progress: 100,
          uid: file.uid
        };
        updateMediaUploadProgress(file);
      })
      .catch(error => {
        message.error(getApiError(error));
        file.uploadStatus = "exception";

        updateMediaUploadProgress(file);
      });
  };

  const renderUploadingMedias = () => {
    return files.map(file => {
      return (
        <Col xs={24} sm={12} md={12} lg={12} xl={8} xxl={4} key={file.uid}>
          <UploadingMediaCard
            progress={file.progress}
            uploadStatus={file.uploadStatus}
            url={file.url}
            deleteAction={() => {
              if (file.uploadStatus === "exception") {
                removeFile(file.uid);
                return;
              }
              dispatch(
                deleteMedia(
                  `/gallery/media/delete/${file.id}`,
                  file.id,
                  file.size
                )
              ).then(() => {
                removeFile(file.uid);
                message.success("Successfully deleted Media");
              });
            }}
            handleShowDialog={handleShowDialog.bind(this, file)}
          />
        </Col>
      );
    });
  };

  const changeUploadedMediasLinkStatus = linkStatus => {
    setLinkStatus(linkStatus);
    const mediaIds = files
      .filter(file => file.uploadStatus === "complete")
      .map(media => media.id);

    dispatch(updateMediasLinkStatus(mediaIds, linkStatus)).then(() =>
      message.success(`Links status changed to ${linkStatus}`)
    );
  };

  const changeUploadedAlbum = album => {
    setSelectedAlbumId(album);
    const mediaIds = files
      .filter(file => file.uploadStatus === "complete")
      .map(media => media.id);

    dispatch(addMediasToAlbum(album, mediaIds)).then(() => {
      message.success("Successfully added selected medias to Album");
    });
  };

  return (
    <>
      <Uploader
        addedFileAction={onMediaSelected}
        fileList={files.filter(file => file.uploadStatus !== "complete")}
        icon={<PictureOutlined style={{ color: "#54a7b2" }} />}
        style={{ width: "40%", height: "30%" }}
      />
      {files.length > 0 && (
        <div
          style={{
            marginLeft: 32,
            marginTop: "2%",
            display: "flex"
          }}
        >
          <div
            style={{
              backgroundColor: "white",
              padding: 10,
              margin: "auto"
            }}
          >
            <div style={{ display: "inline-block" }}>
              <span style={textStyle}>Album</span>
              <SelectWithDropDown
                style={{ width: "auto" }}
                optionValues={Object.values(albums)}
                placeHolder={"Add medias to album"}
                onChange={albumId => changeUploadedAlbum(albumId)}
                onSubmit={value => dispatch(postNewAlbum(value))}
              />
            </div>

            <div style={{ display: "inline-block" }}>
              <span style={textStyle}>Privacy</span>
              <Select
                onSelect={value => changeUploadedMediasLinkStatus(value)}
                size="large"
                style={{ textAlign: "start" }}
                defaultValue={"unlisted"}
              >
                <Option value="unlisted">Unlisted</Option>
                <Option value="public">Public</Option>
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
          closeModalAction={mediaDeleted => {
            //Checks if the media was deleted in modal
            if (mediaDeleted === true) {
              removeFile(activeMedia.uid);
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
