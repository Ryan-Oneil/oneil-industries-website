import React from "react";
import Media from "./Media";
import { Button, Card } from "antd";
const { Meta } = Card;

export default props => {
  const {
    uploaderName,
    fileName,
    dateAdded,
    onApproveClick,
    onDeclineClick,
    media
  } = props;

  return (
    <Card
      cover={
        <div className="pointerCursor">
          <Media media={media} onClick={props.onClick} />
        </div>
      }
      actions={[
        <Button type="primary" onClick={onApproveClick}>
          Approve
        </Button>,
        <Button type="danger" onClick={onDeclineClick}>
          Disapprove
        </Button>
      ]}
    >
      <Meta title={uploaderName} description={`${fileName} - ${dateAdded}`} />
    </Card>
  );
};
