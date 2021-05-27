import React, { useEffect, useState } from "react";
import { Avatar, Button, message } from "antd";
import { getUploadProgress } from "../../../helpers";
import { useDispatch, useSelector } from "react-redux";
import { uploadMedia } from "../../../reducers/mediaReducer";
import Uploader from "../../../components/Uploader";
import PictureOutlined from "@ant-design/icons/lib/icons/PictureOutlined";
import { getApiError } from "../../../apis/ApiErrorHandler";
import MediaUploadedGrid from "../../../components/Gallery/MediaUploadedGrid";
import VideoCameraOutlined from "@ant-design/icons/lib/icons/VideoCameraOutlined";
import PlaySquareOutlined from "@ant-design/icons/lib/icons/PlaySquareOutlined";
import CloudUploadOutlined from "@ant-design/icons/lib/icons/CloudUploadOutlined";
import { generateShareXConfig } from "../../../reducers/userReducer";
import DownloadOutlined from "@ant-design/icons/lib/icons/DownloadOutlined";

export default () => {
  const [files, setFiles] = useState([]);
  const [selectedAlbumId, setSelectedAlbumId] = useState("");
  const dispatch = useDispatch();
  const { shareXConfig } = useSelector((state) => state.user);
  const { name } = useSelector((state) => state.auth.user);
  const textStyle = {
    fontWeight: 500,
    color: "rgba(55,65,81,var(--tw-text-opacity))",
  };

  useEffect(() => {
    if (!shareXConfig) {
      dispatch(generateShareXConfig(`/user/${name}/getShareX`));
    }
  }, []);

  const removeFile = (fileId) => {
    const oldFiles = [...files];

    setFiles(oldFiles.filter((file) => file.uid !== fileId));
  };

  const updateMediaUploadProgress = (mediaFile) => {
    setFiles((prevState) => {
      let updatedMedias = [...prevState];
      const removedMediaList = updatedMedias.filter(
        (file) => file.uid !== mediaFile.uid
      );
      return [...removedMediaList, mediaFile];
    });
  };

  const onMediaSelected = (file) => {
    if (!file.type.includes("image") && !file.type.includes("video")) {
      message.error("This file format isn't supported");
      return;
    }
    file.progress = 0;
    file.url = "";
    file.id = "";
    file.uploadStatus = "active";

    setFiles((prevState) => [...prevState, file]);
    dispatch(
      uploadMedia(
        "/gallery/upload",
        { albumId: selectedAlbumId },
        file,
        (event) => {
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
          uid: file.uid,
        };
        updateMediaUploadProgress(file);
      })
      .catch((error) => {
        message.error(getApiError(error));
        file.uploadStatus = "exception";

        updateMediaUploadProgress(file);
      });
  };

  const ImageDescription = ({ icon, title, description }) => {
    return (
      <span style={{ display: "flex" }}>
        <Avatar
          size={40}
          style={{ background: "#54a7b2", marginRight: 10 }}
          icon={icon}
        />
        <div>
          <span style={textStyle}>{title}</span>
          <br />
          <span>{description}</span>
        </div>
      </span>
    );
  };
  return (
    <>
      <Uploader
        addedFileAction={onMediaSelected}
        fileList={files.filter((file) => file.uploadStatus !== "complete")}
        icon={<CloudUploadOutlined style={{ color: "#54a7b2" }} />}
        style={{
          width: "35%",
          height: "38%",
        }}
        uploadBoxStyle={{ height: "55%" }}
        header={
          <>
            <span style={textStyle}>Supported file types</span>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                marginBottom: "3%",
                marginTop: "3%",
              }}
            >
              <ImageDescription
                icon={<PictureOutlined />}
                title={"Images"}
                description={"JPEG, PNG, WebP"}
              />
              <ImageDescription
                icon={<VideoCameraOutlined />}
                title={"Videos"}
                description={"WebM, MP4"}
              />
              <ImageDescription
                icon={<PlaySquareOutlined />}
                title={"Animated Gifs"}
                description={"Gif"}
              />
            </div>
          </>
        }
        footer={
          <div
            style={{ marginTop: "3%", alignItems: "center" }}
            className={"centerFlexContent"}
          >
            <span style={{ ...textStyle, marginRight: "4%" }}>
              Prefer to upload with ShareX?
            </span>

            <Button
              className={"formattedBackground"}
              type="primary"
              href={`data:text/json;charset=utf-8,${encodeURIComponent(
                JSON.stringify(shareXConfig)
              )}`}
              download="OneilEnterprise.sxcu"
              icon={<DownloadOutlined />}
            >
              Download ShareX Config
            </Button>
          </div>
        }
      />
      <MediaUploadedGrid
        files={files}
        onDeleteMedia={removeFile}
        onSelectAlbum={setSelectedAlbumId}
      />
    </>
  );
};
