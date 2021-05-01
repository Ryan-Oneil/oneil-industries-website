import React from "react";
import Media from "./Media";
import { Card, Image } from "antd";
const { Meta } = Card;

export default ({
  handleShowDialog,
  cardExtras,
  title = "",
  mediaFileName = "",
  mediaType = "",
  dateAdded = ""
}) => {
  const renderMissingMedia = () => {
    return (
      <Image
        alt={"No media"}
        src={require("../../assets/images/noimage.png")}
        preview={false}
        style={{ margin: "auto" }}
        width={"100%"}
      />
    );
  };

  return (
    <Card
      className={"roundedBorder darkGreyBackground"}
      hoverable
      extra={cardExtras}
      style={{ overflow: "hidden" }}
    >
      <div onClick={handleShowDialog.bind(this)}>
        {mediaFileName && mediaType && (
          <Media
            fileName={mediaFileName}
            mediaType={mediaType}
            showVideoPlayButton
          />
        )}
        {!mediaFileName && !mediaType && renderMissingMedia()}
        <Meta
          title={title}
          description={
            <div className={"descriptionText"}>
              {dateAdded && <p>{dateAdded}</p>}
            </div>
          }
          style={{ textAlign: "center", color: "white" }}
        />
      </div>
    </Card>
  );
};
