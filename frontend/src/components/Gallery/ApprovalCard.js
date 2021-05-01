import React from "react";
import Media from "./Media";
import { Button, Card, message } from "antd";
import {
  approvePublicMedia,
  denyPublicMedia
} from "../../reducers/adminReducer";
import { useDispatch } from "react-redux";
import CheckOutlined from "@ant-design/icons/lib/icons/CheckOutlined";
import CloseOutlined from "@ant-design/icons/lib/icons/CloseOutlined";
const { Meta } = Card;

export default props => {
  const { fileName, dateAdded, media } = props;
  const dispatch = useDispatch();

  return (
    <Card
      hoverable
      className={"roundedBorder darkGreyBackground"}
      style={{ overflow: "hidden", textAlign: "center", color: "white" }}
      cover={
        <div onClick={props.onClick.bind(this)}>
          <Media
            mediaType={media.mediaType}
            fileName={media.fileName}
            showVideoPlayButton
          />
        </div>
      }
      actions={[
        <Button
          type="primary"
          onClick={() => {
            dispatch(
              approvePublicMedia(
                `/gallery/admin/media/${media.id}/approve`,
                media.id
              )
            ).then(() =>
              message.success("Media has been approved public view")
            );
          }}
          className={"formattedBackground"}
          icon={<CheckOutlined />}
        >
          Approve
        </Button>,
        <Button
          type="danger"
          onClick={() => {
            dispatch(
              denyPublicMedia(`/gallery/admin/media/${media.id}/deny`, media.id)
            ).then(() => message.success("Media has been denied public view"));
          }}
          icon={<CloseOutlined />}
        >
          Deny
        </Button>
      ]}
    >
      <Meta
        title={fileName}
        description={
          <div className={"descriptionText"}>
            <p>{dateAdded}</p>
          </div>
        }
      />
    </Card>
  );
};
