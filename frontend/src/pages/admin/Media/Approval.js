import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import ApprovalCard from "../../../components/Gallery/ApprovalCard";
import { BASE_URL } from "../../../apis/api";
import Media from "../../../components/Gallery/Media";
import {
  approvePublicMedia,
  denyPublicMedia,
  getMediaApprovals
} from "../../../reducers/adminReducer";
import { Card, Col, Empty, Modal, Row } from "antd";

export default () => {
  const dispatch = useDispatch();
  const { mediaApprovals } = useSelector(state => state.admin);
  const [media, setMedia] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dispatch(getMediaApprovals()).then(() => setLoading(false));
  }, []);

  const handleShowDialog = media => {
    setMedia(media);
  };

  const renderApprovalList = () => {
    return mediaApprovals.map(mediaApproval => {
      return (
        <Col xs={24} sm={24} md={6} lg={6} xl={6}>
          <ApprovalCard
            media={mediaApproval.media}
            onClick={handleShowDialog.bind(this, mediaApproval.media)}
            uploaderName={mediaApproval.media.uploader}
            fileName={mediaApproval.publicName}
            dateAdded={mediaApproval.media.dateAdded}
            onApproveClick={() => {
              dispatch(
                approvePublicMedia(
                  `/gallery/admin/media/${mediaApproval.id}/approve`,
                  mediaApproval.id
                )
              );
            }}
            onDeclineClick={() => {
              dispatch(
                denyPublicMedia(
                  `/gallery/admin/media/${mediaApproval.id}/deny`,
                  mediaApproval.id
                )
              );
            }}
          />
        </Col>
      );
    });
  };

  return (
    <div style={{ padding: "20px" }}>
      <Row gutter={[32, 32]} style={{ padding: "24px" }}>
        {renderApprovalList()}
      </Row>
      {mediaApprovals.length === 0 && (
        <Card>
          <Empty
            description={
              loading
                ? "Fetching Media Approvals"
                : "No Pending Media Approvals"
            }
          />
        </Card>
      )}
      <Modal
        title={media.name}
        visible={media}
        onCancel={() => setMedia("")}
        footer={null}
      >
        <a href={`${BASE_URL}/gallery/${media.mediaType}/${media.fileName}`}>
          <Media media={media} renderVideoControls={true} fullSize={true} />
        </a>
        <div className="centerText">
          <p>Uploader: {media.uploader}</p>
          <p>Uploaded: {media.dateAdded}</p>
        </div>
      </Modal>
    </div>
  );
};
