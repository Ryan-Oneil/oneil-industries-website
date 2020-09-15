import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { CopyToClipboard } from "react-copy-to-clipboard";
import { Button, Card, Col, Input, Row } from "antd";
import {
  generateAPIToken,
  generateShareXConfig,
  getAPIToken
} from "../../reducers/userReducer";
const { TextArea } = Input;

export default () => {
  const dispatch = useDispatch();
  const [sharexText, setSharexText] = useState("Copy");
  const { apiToken, shareXConfig } = useSelector(state => state.user);
  const { name } = useSelector(state => state.auth.user);

  useEffect(() => {
    if (!apiToken) {
      dispatch(getAPIToken(`/user/${name}/getAPIToken`));
    }
    if (!shareXConfig) {
      dispatch(generateShareXConfig(`/user/${name}/getShareX`));
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
                generateAPIToken(`/user/${name}/generateAPIToken`)
              ).then(() =>
                dispatch(generateShareXConfig(`/user/${name}/getShareX`))
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
