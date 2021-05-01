import React, { useState } from "react";
import { Avatar, Button, Card, Col, List, Progress, Result, Row } from "antd";
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
          <Row gutter={[32, 32]} type="flex">
            <Col span={8}>
              <ShareLinkForm uploadAction={uploadAction} />
            </Col>
            <Col span={16}>
              <Uploader
                removeFile={removeFile}
                addedFileAction={file =>
                  setFiles(prevState => [...prevState, file])
                }
                fileList={files}
              />
            </Col>
          </Row>
          <Row gutter={[32, 32]} type="flex" className={"topPadding"}>
            <Col span={24}>
              <Card>
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
            </Col>
          </Row>
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
