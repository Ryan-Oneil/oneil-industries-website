import React, { useEffect, useState } from "react";
import {
  Avatar,
  Button,
  Card,
  Col,
  List,
  Modal,
  Row,
  Statistic,
  Tooltip
} from "antd";
import DownloadOutlined from "@ant-design/icons/lib/icons/DownloadOutlined";
import { useDispatch, useSelector } from "react-redux";
import FileAddOutlined from "@ant-design/icons/lib/icons/FileAddOutlined";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import {
  addFilesToLink,
  deleteFile,
  deleteLink,
  editLink,
  getLinkDetails
} from "../../../reducers/fileReducer";
import {
  displayBytesInReadableForm,
  getDateWithAddedDays
} from "../../../helpers";
import { BASE_URL } from "../../../apis/api";
import ListCard from "../../../components/Stats/ListCard";
import ConfirmButton from "../../../components/ConfirmButton";
import Uploader from "../../../components/Uploader";
import ShareFileForm from "../../../components/formElements/ShareFileForm";
import moment from "moment";

export default props => {
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [loadingData, setLoadingData] = useState(true);
  const [newFiles, setNewFiles] = useState([]);
  const { files, links } = useSelector(state => state.fileSharer.entities);
  const { linkID } = props.match.params;
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getLinkDetails(linkID)).then(() => setLoadingData(false));
  }, []);
  const link = links[linkID] || {
    title: "",
    files: [],
    size: 0,
    views: 0
  };

  const deleteLinkAction = () => {
    dispatch(deleteLink(linkID));
    props.history.goBack();
  };

  const deleteFileAction = fileID => {
    dispatch(deleteFile(fileID, linkID));
  };

  const resetFiles = () => {
    setNewFiles([]);
  };

  const uploadNewFiles = () => {
    setUploading(true);
    dispatch(addFilesToLink(newFiles, linkID)).then(() => {
      resetFiles();
      setUploading(false);
      setShowUploadModal(false);
    });
  };

  return (
    <>
      {link && (
        <Row className={"extraPadding"}>
          <Col span={6}>
            <Card title={link.title}>
              <Statistic title="Views" value={link.views} />
              <Statistic title="Files" value={link.files.length} />
              <Statistic
                title="Size"
                value={displayBytesInReadableForm(link.size)}
              />
            </Card>

            <Card style={{ marginTop: "2%" }} title={"Edit Link"}>
              <ShareFileForm
                submitAction={values => dispatch(editLink(linkID, values))}
                title={link.title}
                expires={moment(link.expiryDatetime)}
                submittingButtonText={"Confirming..."}
                submitButtonText={"Confirm"}
              />
            </Card>
          </Col>
          <Col span={17} offset={1}>
            <ListCard
              title="Files"
              pagination
              size="small"
              dataSource={link.files.map(id => files[id])}
              loading={loadingData}
              renderItem={item => (
                <List.Item
                  actions={[
                    <Tooltip title="Download">
                      <Button
                        shape="circle"
                        type="primary"
                        className={"formattedBackground"}
                        icon={<DownloadOutlined />}
                        onClick={() => {
                          window.open(
                            `${BASE_URL}/file/dl/${item.id}`,
                            "_blank"
                          );
                        }}
                      />
                    </Tooltip>,
                    <ConfirmButton
                      key={"deleteButton"}
                      buttonIcon={<DeleteOutlined />}
                      modalTitle="Do you want to delete this file?"
                      confirmAction={() => deleteFileAction(item.id)}
                      toolTip="Delete File"
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
                    description={`Size ${displayBytesInReadableForm(
                      item.size
                    )}`}
                  />
                </List.Item>
              )}
              footer={[
                <Tooltip title="Add files" key={"fileButton"}>
                  <Button
                    type="primary"
                    className={"formattedBackground"}
                    icon={<FileAddOutlined />}
                    onClick={() => {
                      setShowUploadModal(true);
                    }}
                  >
                    Add File(s)
                  </Button>
                </Tooltip>,
                <ConfirmButton
                  key={"deleteButton"}
                  buttonIcon={<DeleteOutlined />}
                  modalTitle="Do you want to delete this link?"
                  modalDescription="All files will also be deleted"
                  buttonText="Delete"
                  confirmAction={deleteLinkAction}
                  toolTip="Delete Link"
                />
              ]}
            />
          </Col>
        </Row>
      )}
      {showUploadModal && (
        <Modal
          title="Upload New Files"
          visible={showUploadModal}
          onOk={uploadNewFiles}
          confirmLoading={uploading}
          onCancel={() => {
            resetFiles();
            setShowUploadModal(false);
          }}
        >
          <Uploader
            addedFileAction={file =>
              setNewFiles(prevState => [...prevState, file])
            }
            removeFile={file =>
              setNewFiles(prevState =>
                prevState.filter(prevFile => prevFile.uid !== file.uid)
              )
            }
            fileList={newFiles}
            showUploadList
            icon={<FileAddOutlined style={{ color: "#54a7b2" }} />}
          />
        </Modal>
      )}
    </>
  );
};
