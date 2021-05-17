import React from "react";
import Media from "./Media";
import { Card, Progress, Tooltip } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import LinkOutlined from "@ant-design/icons/lib/icons/LinkOutlined";
import { CopyToClipboard } from "react-copy-to-clipboard";

export default ({
  progress,
  uploadStatus,
  url,
  deleteAction,
  handleShowDialog
}) => {
  const mediaFileName = url.substring(url.lastIndexOf("/") + 1);
  const mediaType = url.includes("/image/") ? "image" : "video";
  const completedUpload = uploadStatus === "complete";

  return (
    <Card
      className={"roundedBorder uploadedCard darkGreyBackground"}
      hoverable
      style={{ overflow: "hidden" }}
      cover={
        <>
          {completedUpload && (
            <div onClick={handleShowDialog.bind(this)}>
              <Media
                fileName={mediaFileName}
                mediaType={mediaType}
                showVideoPlayButton
              />
            </div>
          )}

          {!completedUpload && (
            <Progress
              className={"centerFlexContent extraPadding"}
              percent={progress}
              status={uploadStatus}
              type="circle"
              strokeColor={uploadStatus === "exception" ? "#ff4d4f" : "#54a7b2"}
            />
          )}
        </>
      }
      actions={[
        <CopyToClipboard text={url}>
          <Tooltip title={"Copy Link"}>
            <LinkOutlined style={{ color: "#54a7b2" }} />
          </Tooltip>
        </CopyToClipboard>,
        <Tooltip title={"Delete"}>
          <DeleteOutlined
            style={{ color: "red" }}
            onClick={() => {
              if (!completedUpload && uploadStatus !== "exception") {
                return;
              }
              deleteAction();
            }}
          />
        </Tooltip>
      ]}
    />
  );
};
