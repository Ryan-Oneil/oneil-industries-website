import InboxOutlined from "@ant-design/icons/lib/icons/InboxOutlined";
import Dragger from "antd/lib/upload/Dragger";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getQuotaStats } from "../reducers/userReducer";
import { message } from "antd";

export default props => {
  const { used, max } = useSelector(state => state.user.storageQuota);
  const { name } = useSelector(state => state.auth.user);
  const maxInBytes = max * 1000000000;
  const dispatch = useDispatch();
  const [uploadSize, setUploadSize] = useState(0);
  const { addFile, removeFile } = props;

  //Loads most recent quota count
  useEffect(() => {
    dispatch(getQuotaStats(name));
  }, []);

  const config = {
    name: "file",
    multiple: true,
    beforeUpload: file => {
      if (file.size + uploadSize + used > maxInBytes) {
        message.error("This file would exceed your quota of " + max + " GB");
      } else {
        setUploadSize(prevState => prevState + file.size);
        addFile(file);
      }
      return false;
    },
    onRemove: file => {
      setUploadSize(prevState => prevState - file.size);
      removeFile(file);
    }
  };
  return (
    <Dragger {...config} {...props}>
      <p className="ant-upload-drag-icon">
        <InboxOutlined />
      </p>
      <p>Click or drag file to this area to upload</p>
    </Dragger>
  );
};
