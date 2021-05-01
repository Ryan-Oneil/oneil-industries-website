import React from "react";
import Media from "./Media";
import { Button, Card, Progress, Tooltip } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import LinkOutlined from "@ant-design/icons/lib/icons/LinkOutlined";
import EyeOutlined from "@ant-design/icons/lib/icons/EyeOutlined";
import { CopyToClipboard } from "react-copy-to-clipboard";

export default ({
  progress,
  uploadStatus,
  url,
  deleteAction,
  handleShowDialog
}) => {
  const mediaFileName = url.substring(url.lastIndexOf("/") + 1);
  const mediaType = url.includes("image") ? "image" : "video";
  const completedUpload = uploadStatus === "complete";

  return (
    <Card
      className={"roundedBorder uploadedCard"}
      hoverable
      style={{ overflow: "hidden" }}
      actions={[
        <Tooltip title={"View"}>
          <Button
            type={"primary"}
            icon={<EyeOutlined />}
            className={"formattedBackground"}
            onClick={() => window.open(url, "_blank")}
            disabled={!completedUpload}
          />
        </Tooltip>,
        <CopyToClipboard text={url}>
          <Tooltip title={"Copy Link"}>
            <Button
              type={"primary"}
              icon={<LinkOutlined />}
              className={"formattedBackground"}
              disabled={!completedUpload}
            />
          </Tooltip>
        </CopyToClipboard>,
        <Tooltip title={"Delete"}>
          <Button
            type={"danger"}
            icon={<DeleteOutlined />}
            onClick={deleteAction}
            disabled={!completedUpload}
          />
        </Tooltip>
      ]}
    >
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
            status={`${uploadStatus === "complete" ? "" : "active"}`}
            type="circle"
            strokeColor={"#54a7b2"}
          />
        )}
      </>
    </Card>
  );
};
