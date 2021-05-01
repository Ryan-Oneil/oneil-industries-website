import React, { useEffect, useState } from "react";
import { Card, Col, Row, Select, message } from "antd";
import { getApiError, getUploadProgress } from "../../../helpers";
import { useDispatch, useSelector } from "react-redux";
import {
  deleteMedia,
  fetchAlbums,
  postNewAlbum,
  uploadMedia
} from "../../../reducers/mediaReducer";
import SelectWithDropDown from "../../../components/formElements/SelectWithDropDown";
import UploadingMediaCard from "../../../components/Gallery/UploadingMediaCard";
import MediaModal from "../../../components/Gallery/MediaModal";
import Uploader from "../../../components/Uploader";
const { Option } = Select;

export default () => {
  const [files, setFiles] = useState([]);
  const [selectedAlbumId, setSelectedAlbumId] = useState("");
  const [selectedPrivacyStatus, setPrivacyStatus] = useState("unlisted");
  const [activeMedia, setActiveMedia] = useState("");
  const { name } = useSelector(state => state.auth.user);
  const { albums } = useSelector(state => state.medias.entities);
  const dispatch = useDispatch();

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
    file.progress = 0;
    file.url = "";
    file.id = "";
    file.uploadStatus = "uploading";

    setFiles(prevState => [...prevState, file]);
    dispatch(
      uploadMedia(
        "/gallery/upload",
        { linkStatus: selectedPrivacyStatus, albumId: selectedAlbumId },
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
      .catch(error => message.error(getApiError(error)));
  };

  const renderUploadingMedias = () => {
    return files.map(file => {
      return (
        <Col xs={24} sm={12} md={12} lg={8} xl={6} xxl={6} key={file.uid}>
          <UploadingMediaCard
            progress={file.progress}
            uploadStatus={file.uploadStatus}
            url={file.url}
            deleteAction={() => {
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

  return (
    <>
      <Row gutter={[64, 32]} type="flex">
        <Col span={5}>
          <Card>
            <Select
              onSelect={value => setPrivacyStatus(value)}
              size="large"
              style={{ width: "100%", textAlign: "start", marginBottom: "5%" }}
              defaultValue={"unlisted"}
            >
              <Option value="unlisted">Unlisted</Option>
              <Option value="public">Public</Option>
            </Select>
            <SelectWithDropDown
              optionValues={Object.values(albums)}
              placeHolder={"Album"}
              onChange={albumId => setSelectedAlbumId(albumId)}
              onSubmit={value => dispatch(postNewAlbum(value))}
            />
          </Card>
        </Col>
        <Col span={19}>
          <Uploader
            addedFileAction={onMediaSelected}
            fileList={files.filter(file => file.uploadStatus !== "complete")}
          />
        </Col>
      </Row>
      <Row
        gutter={[64, 32]}
        type="flex"
        className={"topPadding"}
        style={{
          height: "76vh",
          overflow: "auto"
        }}
      >
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
