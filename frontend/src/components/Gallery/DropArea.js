import React from "react";
import { useDropzone } from "react-dropzone";

const DropArea = ({ accept, onDrop }) => {
  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept
  });

  return (
    <div {...getRootProps()}>
      <input className="dropzone-input" {...getInputProps()} />
      <div
        className={`ui placeholder segment pointerCursor boxOutline ${
          isDragActive ? "fileHover" : ""
        }`}
      >
        <div className="ui icon header">
          <i className="upload icon" />
          Choose or Drag medias
        </div>
      </div>
    </div>
  );
};

export default DropArea;
