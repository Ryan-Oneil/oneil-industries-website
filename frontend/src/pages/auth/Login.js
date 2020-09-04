import React from "react";
import { Link } from "react-router-dom";
import { Button, Card, Divider } from "antd";
import LoginForm from "../../components/formElements/LoginForm";

export default () => {
  return (
    <div className="login">
      <Card>
        <LoginForm />
        <Divider>OR</Divider>
        <Link to="/register">
          <Button style={{ width: "100%" }}>Sign Up</Button>
        </Link>
      </Card>
    </div>
  );
};
