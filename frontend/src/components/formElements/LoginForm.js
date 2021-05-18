import React from "react";
import { Field, Formik } from "formik";
import { InputWithErrors } from "./index";
import { Alert, Button } from "antd";
import { Link } from "react-router-dom";
import LockOutlined from "@ant-design/icons/lib/icons/LockOutlined";
import { useDispatch } from "react-redux";
import { loginUser } from "../../reducers/authReducer";
import { handleFormError } from "../../apis/ApiErrorHandler";
import MailOutlined from "@ant-design/icons/lib/icons/MailOutlined";

export default () => {
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    const creds = {
      email: formValues.email.trim(),
      password: formValues.password.trim()
    };
    return dispatch(loginUser(creds)).catch(error =>
      handleFormError(error, setFieldError, setStatus)
    );
  };

  const validate = values => {
    const errors = {};

    if (!values.email) {
      errors.email = "Email is required";
    }
    if (!values.password) {
      errors.password = "Password is required";
    }
    return errors;
  };

  return (
    <Formik
      initialValues={{
        email: "",
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
              name="email"
              as={InputWithErrors}
              type="email"
              placeholder="email"
              prefix={<MailOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.email}
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
              className="fullWidth formattedBackground"
              disabled={!isValid || isSubmitting}
              loading={isSubmitting}
              size="large"
            >
              {isSubmitting ? "Logging In" : "Login"}
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
