import React, { useEffect } from "react";
import { Card, Col, Row } from "antd";
import { displayBytesInReadableForm } from "../../helpers";
import { getUserDetails } from "../../reducers/userReducer";
import { useDispatch, useSelector } from "react-redux";

export default () => {
  const dispatch = useDispatch();
  const { name } = useSelector(state => state.auth.user);
  const { storageQuota } = useSelector(state => state.user);

  //Loads most recent quota count
  useEffect(() => {
    dispatch(getUserDetails(name));
  }, []);

  return (
    <Row gutter={[32, 32]}>
      {storageQuota && (
        <Col xs={24} sm={24} md={6} lg={6} xl={6}>
          <Card title="Storage Quota Details">
            <p>
              <strong>Used Storage:</strong>{" "}
              {displayBytesInReadableForm(storageQuota.used)}
            </p>
            <p>
              <strong>Max Storage:</strong> {storageQuota.max} GB
            </p>
            <p>
              <strong>Ignore Quota:</strong>{" "}
              {storageQuota.ignoreQuota ? "Yes" : "No"}
            </p>
          </Card>
        </Col>
      )}
    </Row>
  );
};
