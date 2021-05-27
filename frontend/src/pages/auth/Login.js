import React from "react";
import { Link } from "react-router-dom";
import { Button, Card, Divider } from "antd";
import LoginForm from "../../components/formElements/LoginForm";
import { REGISTER_URL } from "../../constants/constants";

export default () => {
  return (
    <div className="login">
      <Card className={"roundedShadowBox"}>
        <LoginForm />
        <Divider>OR</Divider>
        <Link to={REGISTER_URL}>
          <Button className={"fullWidth formattedBorderColor"}>Sign Up</Button>
        </Link>
      </Card>
    </div>
  );
};
