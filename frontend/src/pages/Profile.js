import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Avatar, Button, Col, Divider, Popconfirm, Progress, Row } from "antd";
import { fetchAPIToken, getUserDetails } from "../reducers/userReducer";
import EditUserForm from "../components/formElements/EditUserForm";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import { displayBytesInReadableForm } from "../helpers";
import ReloadOutlined from "@ant-design/icons/lib/icons/ReloadOutlined";
import DataInfo from "../components/DataDisplay/DataInfo";

export default () => {
  const { name, email, role } = useSelector((state) => state.user.details);
  const { used, max, ignoreQuota } = useSelector(
    (state) => state.user.storageQuota
  );
  const userName = useSelector((state) => state.auth.user.name);
  const dispatch = useDispatch();
  const boxStyle = {
    background: "white",
    padding: "5%",
    height: "100%",
  };

  useEffect(() => {
    dispatch(getUserDetails(userName));
  }, []);

  return (
    <Row gutter={[32, 32]} justify="center">
      <Col xs={24} sm={24} md={10} lg={10} xl={6}>
        <div
          className={"roundedShadowBox"}
          style={{
            ...boxStyle,
            textAlign: "center",
          }}
        >
          <Avatar
            size={{ xs: 24, sm: 32, md: 40, lg: 64, xl: 100, xxl: 130 }}
            icon={<UserOutlined />}
          />
          <p
            style={{
              fontWeight: 500,
              color: "rgba(55,65,81,var(--tw-text-opacity))",
              textTransform: "capitalize",
              fontSize: "1.2em",
              marginTop: "2%",
            }}
          >
            {name}
          </p>
          <p>{email}</p>
          <Divider style={{ borderTop: "3px solid #ececec" }} />
          <p
            style={{
              textTransform: "capitalize",
            }}
          >
            {role.replace("ROLE_", "").toLowerCase()}
          </p>
          <Popconfirm
            title="Generating an API token will invalidate previous ShareX config"
            onConfirm={() => {
              dispatch(fetchAPIToken(`/user/${name}/generateAPIToken`));
            }}
            okText="Generate"
            cancelText="Cancel"
            okButtonProps={{ className: "formattedBackground" }}
          >
            <Button
              className="centerContent formattedBackground"
              type="primary"
              style={{ marginTop: "2%", borderRadius: ".375rem" }}
              icon={<ReloadOutlined />}
              size="large"
            >
              Generate API Token
            </Button>
          </Popconfirm>
        </div>
      </Col>

      <Col xs={24} sm={24} md={14} lg={14} xl={8}>
        <div className={"roundedShadowBox"} style={boxStyle}>
          <EditUserForm user={{ email, name }} loading={false} />
        </div>
      </Col>

      <Col xs={24} sm={24} md={10} lg={10} xl={7}>
        <div
          className={"roundedShadowBox"}
          style={{
            ...boxStyle,
            textAlign: "center",
          }}
        >
          <Progress
            type="dashboard"
            percent={((used / (max * Math.pow(1024, 3))) * 100).toFixed(2)}
            style={{ width: "100%", marginBottom: "5%" }}
            strokeColor={"#54a7b2"}
          />
          <DataInfo title={"Max Quota"} info={`${max} GB`} />
          <DataInfo
            title={"Used Storage"}
            info={displayBytesInReadableForm(used)}
          />
          <DataInfo
            title={"Ignore storage limit"}
            info={ignoreQuota ? "Yes" : "No"}
          />
        </div>
      </Col>
    </Row>
  );
};
