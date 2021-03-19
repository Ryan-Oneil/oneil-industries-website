import React from "react";
import { Col } from "antd";
import MediaCard from "./MediaCard";

export default (mediasList, mediaOnClick) => {
  return mediasList.map(media => {
    return (
      <Col xs={24} sm={12} md={12} lg={8} xl={6} xxl={4} key={media.id}>
        <MediaCard
          mediaFileName={media.fileName}
          mediaType={media.mediaType}
          dateAdded={media.dateAdded}
          handleShowDialog={mediaOnClick.bind(this, media)}
        />
      </Col>
    );
  });
};
