import React from "react";
import {
  Button,
  Card,
  Col,
  Comment,
  Row,
  Input,
  Tooltip,
  Modal,
  message,
  Space
} from "antd";
import Media from "./Media";
import CloseOutlined from "@ant-design/icons/lib/icons/CloseOutlined";
import MediaComment from "./MediaComment";
import { useDispatch, useSelector } from "react-redux";
import SendOutlined from "@ant-design/icons/lib/icons/SendOutlined";
import ShareAltOutlined from "@ant-design/icons/lib/icons/ShareAltOutlined";
import { CopyToClipboard } from "react-copy-to-clipboard";
import { MEDIA_IMAGE_URL } from "../../apis/endpoints";
import EditMediaForm from "../formElements/EditMediaForm";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import { deleteMedia } from "../../reducers/mediaReducer";
const { TextArea } = Input;

export default ({
  activeMedia,
  closeModalAction,
  showMediaPreview,
  enableManagement,
  extraMediaInfo
}) => {
  const { isAuthenticated } = useSelector(state => state.auth);
  const { comments } = useSelector(state => state.medias.entities);
  const dispatch = useDispatch();

  const renderComments = () => {
    return Object.values(comments).map(comment => (
      <MediaComment {...comment} key={comment.id} />
    ));
  };
  return (
    <Modal
      title={activeMedia.name}
      visible={true}
      onCancel={closeModalAction}
      footer={null}
      width={"80vw"}
      maskStyle={{ backgroundColor: "rgba(0, 0, 0, 0.7)" }}
      modalRender={() => {
        return (
          <div className={"ant-modal-content mediaModal"}>
            <Row gutter={[32, 32]}>
              <Col xs={24} sm={24} md={12} lg={10} xl={6} xxl={6}>
                <Card className={"mediaDescription preventTextOverflow "}>
                  <h1>{activeMedia.name}</h1>
                  <h1>{activeMedia.dateAdded}</h1>
                  {extraMediaInfo}
                  {enableManagement && <EditMediaForm media={activeMedia} />}
                </Card>
              </Col>
              <Col xs={24} sm={24} md={12} lg={14} xl={12} xxl={10}>
                <div
                  className={
                    "fullRoundedBorder mediaModalContent centerFlexContent"
                  }
                >
                  <Media
                    fileName={activeMedia.fileName}
                    mediaType={activeMedia.mediaType}
                    renderVideoControls={true}
                    fullSize={true}
                    showPreview={showMediaPreview}
                  />
                </div>
              </Col>
              <Col xs={24} sm={24} md={24} lg={24} xl={6} xxl={8}>
                <Card
                  headStyle={{ backgroundColor: "#49494b" }}
                  title={
                    <h2 className={"whiteColor centerText removeMargin"}>
                      Comments
                    </h2>
                  }
                  className={
                    "roundedBorder preventTextOverflow modalCommentSection"
                  }
                  extra={
                    <Button
                      onClick={closeModalAction}
                      icon={<CloseOutlined />}
                    />
                  }
                >
                  {renderComments()}
                  <Comment
                    className={"commentReplyBox"}
                    style={{
                      paddingTop: 0,
                      borderBottomLeftRadius: 10,
                      borderBottomRightRadius: 10
                    }}
                    content={
                      <>
                        <TextArea
                          rows={4}
                          className={"roundedBorder"}
                          disabled={!isAuthenticated}
                        />
                        <div style={{ marginTop: "3%" }}>
                          <Space>
                            <CopyToClipboard
                              text={MEDIA_IMAGE_URL + activeMedia.fileName}
                            >
                              <Tooltip title={"Copy Media Link"}>
                                <Button
                                  className={"formattedBackground"}
                                  icon={<ShareAltOutlined />}
                                  shape="circle"
                                  type="primary"
                                />
                              </Tooltip>
                            </CopyToClipboard>
                            {enableManagement && (
                              <Tooltip title={"Delete Media"}>
                                <Button
                                  icon={<DeleteOutlined />}
                                  shape="circle"
                                  type="danger"
                                  onClick={() => {
                                    dispatch(
                                      deleteMedia(
                                        `/gallery/media/delete/${activeMedia.id}`,
                                        activeMedia.id,
                                        activeMedia.size
                                      )
                                    ).then(() => {
                                      message.success(
                                        "Successfully delete Media"
                                      );

                                      closeModalAction(true);
                                    });
                                  }}
                                />
                              </Tooltip>
                            )}
                          </Space>
                          <Button
                            className={"formattedBackground"}
                            type="primary"
                            icon={<SendOutlined />}
                            disabled={!isAuthenticated}
                            style={{ float: "right" }}
                          >
                            Add Comment
                          </Button>
                        </div>
                      </>
                    }
                  />
                </Card>
              </Col>
            </Row>
          </div>
        );
      }}
    />
  );
};
