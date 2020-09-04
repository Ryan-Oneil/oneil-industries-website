import React from "react";
import { Card } from "antd";
import { resetPassword } from "../../reducers/authReducer";
import { useDispatch } from "react-redux";
import ForgotPassword from "../../components/formElements/EmailForm";

export default () => {
  const dispatch = useDispatch();

  return (
    <div className="login">
      <Card>
        <ForgotPassword action={() => dispatch(resetPassword())} />
      </Card>
    </div>
  );
};
