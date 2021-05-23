import Dragger from "antd/lib/upload/Dragger";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { message } from "antd";
import { getQuotaStats } from "../reducers/userReducer";

export default ({
  removeFile,
  addedFileAction,
  fileList,
  showUploadList = false,
  icon,
  style,
  header,
  footer,
  uploadBoxStyle,
}) => {
  const { used, max } = useSelector((state) => state.user.storageQuota);
  const { name } = useSelector((state) => state.auth.user);
  const maxInBytes = max * 1000000000;
  const dispatch = useDispatch();
  const [uploadSize, setUploadSize] = useState(0);

  //Loads most recent quota count
  useEffect(() => {
    dispatch(getQuotaStats(name));
  }, []);

  //Recalculates upload size since list is handled by other components
  useEffect(() => {
    let uploadSize = fileList.reduce((a, b) => a + b.size, 0);

    setUploadSize(uploadSize);
  }, [fileList]);

  const config = {
    name: "file",
    multiple: true,
    beforeUpload: (file) => {
      if (file.size + uploadSize + used > maxInBytes) {
        message.error("This file would exceed your quota of " + max + " GB");
      } else {
        addedFileAction(file);
      }
      return false;
    },
    onRemove: (file) => {
      removeFile(file);
    },
  };
  return (
    <div
      className="uploaderBox roundedShadowBox centerContent"
      style={{ overflow: "auto", ...style }}
    >
      {header}
      <div style={uploadBoxStyle}>
        <Dragger {...config} showUploadList={showUploadList}>
          <p className="ant-upload-drag-icon">{icon}</p>
          <p style={{ fontWeight: 600, fontSize: "16px" }}>
            Drag and drop to upload
          </p>
          <p>or click to browse</p>
        </Dragger>
      </div>
      {footer}
    </div>
  );
};
