import React from "react";
import { Card } from "antd";
import { resetPassword } from "../../reducers/authReducer";
import ForgotPassword from "../../components/formElements/EmailForm";

export default () => {
  return (
    <div className="login">
      <Card>
        <ForgotPassword action={email => resetPassword(email)} />
      </Card>
    </div>
  );
};
