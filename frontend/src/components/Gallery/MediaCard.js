import React from "react";
import Media from "./Media";
import { Card } from "antd";
const { Meta } = Card;

export default ({ mediaItem, handleShowDialog, cardExtras }) => {
  return (
    <Card
      className={"roundedBorder darkGreyBackground"}
      hoverable
      extra={cardExtras}
    >
      <div onClick={handleShowDialog.bind(this, mediaItem)}>
        <Media media={mediaItem} />
        <Meta
          title={mediaItem.name}
          description={
            <span className={"descriptionText"}>{mediaItem.dateAdded}</span>
          }
          style={{ textAlign: "center", color: "white" }}
        />
      </div>
    </Card>
  );
};
