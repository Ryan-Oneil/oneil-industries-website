import React from "react";
import { Link } from "react-router-dom";
import RegisterForm from "../../components/formElements/RegisterForm";
import { Card } from "antd";

export default () => {
  return (
    <div className="login">
      <Card>
        <RegisterForm />
        <Link to="/login">Already Registered?</Link>
      </Card>
    </div>
  );
};
