import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import ApprovalCard from "../../../components/Gallery/ApprovalCard";
import { getMediaApprovals } from "../../../reducers/adminReducer";
import { Card, Col, Empty, Row } from "antd";
import MediaModal from "../../../components/Gallery/MediaModal";

export default () => {
  const dispatch = useDispatch();
  const { mediaApprovals } = useSelector((state) => state.admin);
  const [media, setMedia] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dispatch(getMediaApprovals()).then(() => setLoading(false));
  }, []);

  const handleShowDialog = (media) => {
    setMedia(media);
  };

  const renderApprovalList = () => {
    return mediaApprovals.map((media) => {
      return (
        <Col xs={24} sm={24} md={12} lg={8} xl={6} xxl={4}>
          <ApprovalCard
            media={media}
            onClick={handleShowDialog.bind(this, media)}
            fileName={media.name}
            dateAdded={media.dateAdded}
          />
        </Col>
      );
    });
  };

  return (
    <>
      {mediaApprovals.length > 0 && (
        <Row
          gutter={[32, 32]}
          className={"extraPadding"}
          style={{
            height: "90vh",
            overflow: "auto",
          }}
        >
          {renderApprovalList()}
        </Row>
      )}
      {mediaApprovals.length === 0 && (
        <Card style={{ margin: 24 }}>
          <Empty
            description={
              loading
                ? "Fetching Media Approvals"
                : "No Pending Media Approvals"
            }
          />
        </Card>
      )}
      {media && (
        <MediaModal
          activeMedia={media}
          closeModalAction={() => setMedia("")}
          showMediaPreview
          enableManagement
        />
      )}
    </>
  );
};
