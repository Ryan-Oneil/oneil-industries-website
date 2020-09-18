import React from "react";
import { Field, Formik } from "formik";
import { InputWithErrors } from "./index";
import { Alert, Button } from "antd";
import { getApiError } from "../../helpers";
import { Link } from "react-router-dom";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import LockOutlined from "@ant-design/icons/lib/icons/LockOutlined";
import { useDispatch } from "react-redux";
import { loginUser } from "../../reducers/authReducer";

export default () => {
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus }) => {
    const creds = {
      username: formValues.username.trim(),
      password: formValues.password.trim()
    };
    return dispatch(loginUser(creds)).catch(error =>
      setStatus(getApiError(error))
    );
  };

  return (
    <Formik
      initialValues={{
        username: "",
        password: ""
      }}
      onSubmit={onSubmit}
      validate={validate}
    >
      {props => {
        const {
          isSubmitting,
          handleSubmit,
          isValid,
          errors,
          status,
          setStatus
        } = props;

        return (
          <form onSubmit={handleSubmit} className="login-form">
            <Field
              name="username"
              as={InputWithErrors}
              type="text"
              placeholder="Username"
              prefix={<UserOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.username}
            />
            <Field
              name="password"
              as={InputWithErrors}
              type="password"
              placeholder="Password"
              prefix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.password}
            />
            <Link to="/resetPassword" style={{ float: "right" }}>
              Forgot Password?
            </Link>
            <Button
              type="primary"
              htmlType="submit"
              className="form-button"
              disabled={!isValid || isSubmitting}
              loading={isSubmitting}
              size="large"
            >
              {isSubmitting ? "Logging In" : "Login"}
            </Button>
            {status && (
              <Alert
                message="Login Error"
                description={status}
                type="error"
                closable
                showIcon
                onClose={() => setStatus("")}
              />
            )}
          </form>
        );
      }}
    </Formik>
  );
};

const validate = values => {
  const errors = {};

  if (!values.username) {
    errors.username = "Username is required";
  }
  if (!values.password) {
    errors.password = "Password is required";
  }
  return errors;
};
