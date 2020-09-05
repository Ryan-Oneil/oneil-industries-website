import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  generateAPIToken,
  generateShareXConfig,
  getAPIToken
} from "../../actions/profile";
import { CopyToClipboard } from "react-copy-to-clipboard";
import { Button, Card, Col, Input, Row } from "antd";
import CopyOutlined from "@ant-design/icons/lib/icons/CopyOutlined";
const { TextArea } = Input;

export default () => {
  const dispatch = useDispatch();
  const [sharexText, setSharexText] = useState("Copy");
  const { apiToken, shareXConfig } = useSelector(state => state.profile);

  useEffect(() => {
    if (!apiToken) {
      dispatch(getAPIToken("/user/profile/getAPIToken"));
    }
    if (!shareXConfig) {
      dispatch(generateShareXConfig("/user/profile/getShareX"));
    }
  }, []);

  return (
    <Row gutter={[32, 32]} justify="center">
      <Col xs={24} sm={24} md={6} lg={6} xl={10}>
        <Card title="ShareX Config">
          <TextArea readOnly value={JSON.stringify(shareXConfig)} />
          <CopyToClipboard
            text={JSON.stringify(shareXConfig)}
            onCopy={() => setSharexText("Copied")}
          >
            <Button className="centerButton">{sharexText}</Button>
          </CopyToClipboard>
        </Card>
      </Col>
      <Col xs={24} sm={24} md={6} lg={6} xl={10}>
        <Card title="Api Token">
          <Input type="text" readOnly value={apiToken} />
          <Button
            className="centerButton"
            onClick={() => {
              dispatch(
                generateAPIToken("/user/profile/generateAPIToken")
              ).then(() =>
                dispatch(generateShareXConfig("/user/profile/getShareX"))
              );
            }}
          >
            Generate
          </Button>
        </Card>
      </Col>
    </Row>
  );
};
