import React from "react";
import { Field, Formik } from "formik";
import { InputWithErrors } from "./index";
import { Alert, Button } from "antd";
import MailOutlined from "@ant-design/icons/lib/icons/MailOutlined";
import LockOutlined from "@ant-design/icons/lib/icons/LockOutlined";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import { registerUser } from "../../reducers/authReducer";
import { handleFormError } from "../../apis/ApiErrorHandler";

export default () => {
  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    const creds = {
      username: formValues.username.trim(),
      password: formValues.password.trim(),
      email: formValues.email.trim()
    };
    return registerUser(creds)
      .then(response => setStatus({ msg: response.data, type: "success" }))
      .catch(error => handleFormError(error, setFieldError, setStatus));
  };

  const validate = values => {
    const errors = {};

    if (!values.username) {
      errors.username = "Username is required";
    }
    if (!values.password) {
      errors.password = "Password is required";
    }
    if (values.password !== values.confirmPassword) {
      errors.confirmPassword = "Passwords don't match";
    }
    if (!values.email) {
      errors.email = "Email is required";
    }
    return errors;
  };

  return (
    <Formik
      initialValues={{
        username: "",
        password: "",
        confirmPassword: "",
        email: ""
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
            <Field
              name="confirmPassword"
              as={InputWithErrors}
              type="password"
              placeholder="Confirm Password"
              prefix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.confirmPassword}
            />
            <Field
              name="email"
              as={InputWithErrors}
              type="email"
              placeholder="Email"
              prefix={<MailOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.email}
            />
            <Button
              type="primary"
              htmlType="submit"
              className="fullWidth formattedBackground"
              disabled={!isValid || isSubmitting}
              size="large"
              loading={isSubmitting}
            >
              {isSubmitting ? "Registering" : "Register"}
            </Button>
            {status && (
              <Alert
                message={status.msg}
                type={status.type}
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
