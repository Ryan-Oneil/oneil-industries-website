import Media from "./Media";
import React from "react";
import { Card, Col, Divider } from "antd";
const { Meta } = Card;

export default (mediasList, mediaOnClick, displayUploader) => {
  return mediasList.map(media => {
    return (
      <Col xs={18} sm={12} md={12} lg={8} xl={6}>
        <Card>
          <div className="pointerCursor">
            <Media media={media} onClick={mediaOnClick.bind(this, media)} />
          </div>
          <Divider />
          <Meta
            title={displayUploader ? media.uploader : media.name}
            description={media.dateAdded}
            style={{ textAlign: "center" }}
          />
        </Card>
      </Col>
    );
  });
};
