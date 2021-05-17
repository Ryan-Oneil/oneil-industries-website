import React, { useState } from "react";
import { Avatar, Button, Card, List, Progress, Result } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import Uploader from "../../../components/Uploader";
import {
  displayBytesInReadableForm,
  getUploadProgress
} from "../../../helpers";
import ShareLinkForm from "../../../components/formElements/ShareLinkForm";
import { uploadFiles } from "../../../reducers/fileReducer";
import { CopyToClipboard } from "react-copy-to-clipboard";
import EditOutlined from "@ant-design/icons/lib/icons/EditOutlined";
import ShareAltOutlined from "@ant-design/icons/lib/icons/ShareAltOutlined";
import FileAddOutlined from "@ant-design/icons/lib/icons/FileAddOutlined";

export default props => {
  const [files, setFiles] = useState([]);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [uploadStatus, setUploadStatus] = useState("");
  const [uploadedFilesUrlId, setUploadedFilesUrlId] = useState("");

  const removeFile = fileId => {
    const oldFiles = [...files];

    setFiles(oldFiles.filter(file => file.uid !== fileId));
  };

  const updateFileUploadProgress = (progress, status) => {
    setUploadProgress(progress);
    setUploadStatus(status);
  };

  const uploadAction = params => {
    return uploadFiles("/share", files, params, event =>
      updateFileUploadProgress(getUploadProgress(event), "active")
    )
      .then(({ data }) => {
        setFiles([]);
        updateFileUploadProgress(100, "complete");

        setUploadedFilesUrlId(data.id);
      })
      .catch(error => {
        updateFileUploadProgress(100, "exception");
      });
  };

  return (
    <>
      {uploadStatus === "" && (
        <>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <div style={{ width: "35%" }}>
              <ShareLinkForm uploadAction={uploadAction} />
            </div>
            <div style={{ width: "60%" }}>
              <Uploader
                style={{ height: "100%" }}
                removeFile={removeFile}
                addedFileAction={file =>
                  setFiles(prevState => [...prevState, file])
                }
                fileList={files}
                icon={<FileAddOutlined style={{ color: "#54a7b2" }} />}
              />
            </div>
          </div>
          <Card style={{ marginTop: "2%" }} className={"roundedShadowBox"}>
            <List
              dataSource={files}
              renderItem={item => (
                <List.Item
                  actions={[
                    <Button
                      danger
                      type="primary"
                      shape="circle"
                      icon={<DeleteOutlined />}
                      onClick={() => {
                        removeFile(item.uid);
                      }}
                    />
                  ]}
                >
                  <List.Item.Meta
                    avatar={
                      <Avatar
                        src={require("../../../assets/images/file.png")}
                      />
                    }
                    title={item.name}
                    description={displayBytesInReadableForm(item.size)}
                  />
                </List.Item>
              )}
            />
          </Card>
        </>
      )}
      {uploadStatus !== "" && (
        <Card
          style={{ width: "40%" }}
          className={"centerContent preventTextOverflow"}
        >
          {uploadStatus !== "complete" && (
            <>
              <Progress
                className={"centerFlexContent extraPadding"}
                percent={uploadProgress}
                status={uploadStatus}
                type="circle"
                strokeColor={
                  uploadStatus === "exception" ? "#ff4d4f" : "#54a7b2"
                }
                width={256}
              />

              <h1 className={"centerText midText"}>Your files are uploading</h1>
            </>
          )}
          {uploadStatus === "complete" && (
            <Result
              status="success"
              title="Files finished uploading"
              extra={[
                <CopyToClipboard
                  text={`${window.location.origin.toString()}/shared/${uploadedFilesUrlId}`}
                  key={"copyUrl"}
                >
                  <Button
                    type="primary"
                    disabled={!uploadedFilesUrlId}
                    className={"formattedBackground"}
                    icon={<ShareAltOutlined />}
                  >
                    Copy Url
                  </Button>
                </CopyToClipboard>,
                <Button
                  key={"editSharedFiles"}
                  onClick={() =>
                    props.history.push(
                      `/dashboard/fileshare/files/edit/${uploadedFilesUrlId}`
                    )
                  }
                  icon={<EditOutlined />}
                >
                  Edit
                </Button>
              ]}
            />
          )}
        </Card>
      )}
    </>
  );
};
