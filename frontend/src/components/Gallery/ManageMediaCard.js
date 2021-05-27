import React, { useState } from "react";
import MediaCard from "./MediaCard";
import { ReactComponent as UncheckedCircle } from "../../assets/images/unchecked.svg";
import { ReactComponent as CheckedCircle } from "../../assets/images/checked.svg";
import Icon from "@ant-design/icons";

export default ({
  handleShowDialog,
  title,
  mediaFileName,
  mediaType,
  dateAdded,
  manageEnabled,
  onSelect,
  isSelected,
}) => {
  const iconStyle = { fontSize: "4em", float: "right" };

  return (
    <MediaCard
      mediaFileName={mediaFileName}
      title={title}
      mediaType={mediaType}
      dateAdded={dateAdded}
      handleShowDialog={manageEnabled ? onSelect : handleShowDialog}
      extraClasses={"manageMediaCard"}
    >
      {manageEnabled && (
        <div
          className={`manageBox centerContent ${
            isSelected ? "box-selected" : ""
          }`}
        >
          {!isSelected && (
            <Icon component={UncheckedCircle} style={iconStyle} />
          )}
          {isSelected && <Icon component={CheckedCircle} style={iconStyle} />}
        </div>
      )}
    </MediaCard>
  );
};
