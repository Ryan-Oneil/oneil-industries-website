import React from "react";
import Media from "./Media";
import { Card, message, Tooltip } from "antd";
import { changePublicMediaStatus } from "../../reducers/adminReducer";
import { useDispatch } from "react-redux";
import CheckOutlined from "@ant-design/icons/lib/icons/CheckOutlined";
import CloseOutlined from "@ant-design/icons/lib/icons/CloseOutlined";
const { Meta } = Card;

export default (props) => {
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
        <Tooltip title={"Approve"}>
          <CheckOutlined
            style={{ color: "#54a7b2" }}
            onClick={() => {
              dispatch(
                changePublicMediaStatus(
                  `/gallery/admin/media/${media.id}/approve`,
                  media.id
                )
              ).then(() =>
                message.success("Media has been approved public view")
              );
            }}
          />
        </Tooltip>,
        <Tooltip title={"Deny"}>
          <CloseOutlined
            style={{ color: "red" }}
            onClick={() => {
              dispatch(
                changePublicMediaStatus(
                  `/gallery/admin/media/${media.id}/deny`,
                  media.id
                )
              ).then(() =>
                message.success("Media has been denied public view")
              );
            }}
          />
        </Tooltip>,
      ]}
    >
      <Meta title={fileName} description={dateAdded} />
    </Card>
  );
};
