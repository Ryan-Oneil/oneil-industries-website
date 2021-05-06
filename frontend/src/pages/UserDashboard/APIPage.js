import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { CopyToClipboard } from "react-copy-to-clipboard";
import { Button, Card, Col, Input, Row, Space } from "antd";
import {
  generateAPIToken,
  generateShareXConfig,
  getAPIToken
} from "../../reducers/userReducer";
import CopyOutlined from "@ant-design/icons/lib/icons/CopyOutlined";
import DownloadOutlined from "@ant-design/icons/lib/icons/DownloadOutlined";
import ReloadOutlined from "@ant-design/icons/lib/icons/ReloadOutlined";
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
        <Card title="ShareX Config" className={"roundedShadowBox"}>
          <TextArea readOnly value={JSON.stringify(shareXConfig, null, 2)} />
          <div className={"centerFlexContent topPadding"}>
            <Space>
              <Button
                className={"formattedBackground"}
                type="primary"
                href={`data:text/json;charset=utf-8,${encodeURIComponent(
                  JSON.stringify(shareXConfig)
                )}`}
                download="OneilEnterprise.sxcu"
                icon={<DownloadOutlined />}
              >
                Download ShareX Config
              </Button>
              <CopyToClipboard
                text={JSON.stringify(shareXConfig)}
                onCopy={() => setSharexText("Copied")}
              >
                <Button
                  type="primary"
                  className={"formattedBackground"}
                  icon={<CopyOutlined />}
                >
                  {sharexText}
                </Button>
              </CopyToClipboard>
            </Space>
          </div>
        </Card>
      </Col>
      <Col xs={24} sm={24} md={6} lg={6} xl={10}>
        <Card title="Api Token" className={"roundedShadowBox"}>
          <TextArea readOnly value={apiToken} />
          <Button
            className="centerContent formattedBackground"
            type="primary"
            style={{ marginTop: "2%" }}
            onClick={() => {
              dispatch(
                generateAPIToken(`/user/${name}/generateAPIToken`)
              ).then(() =>
                dispatch(generateShareXConfig(`/user/${name}/getShareX`))
              );
            }}
            icon={<ReloadOutlined />}
          >
            Generate
          </Button>
        </Card>
      </Col>
    </Row>
  );
};
