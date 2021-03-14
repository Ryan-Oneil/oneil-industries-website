import React from "react";
import { Avatar, Comment } from "antd";

export default ({ username, avatarLink, commentContent, date }) => {
  return (
    <Comment
      className={"mediaComment"}
      author={
        <h2 className={"midText topPadding darkTextColor"}>{username}</h2>
      }
      avatar={<Avatar size={100} src={avatarLink} />}
      content={
        <p className={"darkTextColor commentFormat"}>{commentContent}</p>
      }
      datetime={date}
    />
  );
};
