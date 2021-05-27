import React from "react";
import Media from "./Media";
import { Card, Image } from "antd";
import noImage from "../../assets/images/noimage.png";
const { Meta } = Card;

export default ({
  handleShowDialog,
  cardExtras,
  title = "",
  mediaFileName = "",
  mediaType = "",
  dateAdded = "",
  children,
  extraClasses,
}) => {
  const renderMissingMedia = () => {
    return (
      <Image
        alt={"No media"}
        src={noImage}
        preview={false}
        style={{ margin: "auto" }}
        width={"100%"}
      />
    );
  };

  return (
    <Card
      className={`roundedBorder darkGreyBackground ${extraClasses}`}
      hoverable
      extra={cardExtras}
      style={{ overflow: "hidden" }}
      cover={
        <div onClick={handleShowDialog.bind(this)}>
          {mediaFileName && mediaType && (
            <Media
              fileName={mediaFileName}
              mediaType={mediaType}
              showVideoPlayButton
            />
          )}
          {!mediaFileName && !mediaType && renderMissingMedia()}
          {children}
        </div>
      }
    >
      <Meta
        title={title}
        description={dateAdded}
        style={{ textAlign: "center", color: "white" }}
      />
    </Card>
  );
};
