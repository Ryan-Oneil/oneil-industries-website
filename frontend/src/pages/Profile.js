import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Button, Card, Col, Dropdown, Menu, Modal, Row } from "antd";
import SettingOutlined from "@ant-design/icons/lib/icons/SettingOutlined";
import { getUserDetails, updateEmail } from "../reducers/userReducer";
import ChangePasswordForm from "../components/formElements/ChangePasswordForm";
import EmailForm from "../components/formElements/EmailForm";
import { changeUserPassword } from "../reducers/adminReducer";

export default () => {
  const [showPasswordForm, setShowPasswordForm] = useState(false);
  const [showEmailForm, setShowEmailForm] = useState(false);
  const { name, email, role, enabled } = useSelector(
    (state) => state.user.details
  );
  const userName = useSelector((state) => state.auth.user.name);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getUserDetails(userName));
  }, []);

  const actions = (
    <Menu>
      <Menu.Item key="0">
        <Button type="link" onClick={() => setShowPasswordForm(true)}>
          Change Password
        </Button>
      </Menu.Item>
      <Menu.Item key="1">
        <Button type="link" onClick={() => setShowEmailForm(true)}>
          Change Email
        </Button>
      </Menu.Item>
    </Menu>
  );

  const passwordModal = () => {
    return (
      <Modal
        title="Change Password"
        visible={showPasswordForm}
        onCancel={() => setShowPasswordForm(false)}
        footer={null}
      >
        <ChangePasswordForm
          action={(password) =>
            changeUserPassword(userName, password).then(() =>
              setShowPasswordForm(false)
            )
          }
        />
      </Modal>
    );
  };

  const emailModal = () => {
    return (
      <Modal
        title="Change Email"
        visible={showEmailForm}
        onCancel={() => setShowEmailForm(false)}
        footer={null}
      >
        <EmailForm
          action={(email) =>
            dispatch(updateEmail(userName, email)).then(() =>
              setShowEmailForm(false)
            )
          }
        />
      </Modal>
    );
  };

  return (
    <>
      <Row gutter={[32, 32]} type="flex">
        <Col span={8}>
          <Card
            title="User Details"
            extra={
              <Dropdown overlay={actions} trigger={["click"]}>
                <SettingOutlined />
              </Dropdown>
            }
          >
            <p>Email: {email}</p>
            <p>Username: {name} </p>
          </Card>
        </Col>
        <Col span={8}>
          <Card title="Account Details">
            <p>Role: {role}</p>
            <p>Account status: {enabled ? "Active" : "Disabled"} </p>
          </Card>
        </Col>
      </Row>
      {showPasswordForm && passwordModal()}
      {showEmailForm && emailModal()}
    </>
  );
};
