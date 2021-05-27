import React from "react";
import { Field } from "formik";
import { InputWithErrors } from "./index";
import { Link } from "react-router-dom";
import LockOutlined from "@ant-design/icons/lib/icons/LockOutlined";
import { useDispatch } from "react-redux";
import { loginUser } from "../../reducers/authReducer";
import { handleFormError } from "../../apis/ApiErrorHandler";
import MailOutlined from "@ant-design/icons/lib/icons/MailOutlined";
import BaseForm from "./BaseForm";

export default () => {
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    const creds = {
      email: formValues.email.trim(),
      password: formValues.password.trim(),
    };
    return dispatch(loginUser(creds)).catch((error) =>
      handleFormError(error, setFieldError, setStatus)
    );
  };

  const validate = (values) => {
    const errors = {};

    if (!values.email) {
      errors.email = "Email is required";
    }
    if (!values.password) {
      errors.password = "Password is required";
    }
    return errors;
  };

  const fields = (errors) => {
    return (
      <>
        <Field
          name="email"
          as={InputWithErrors}
          type="email"
          suffix={<MailOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
          error={errors.email}
        />
        <Field
          name="password"
          as={InputWithErrors}
          type="password"
          suffix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
          error={errors.password}
        />
        <Link to="/resetPassword" style={{ float: "right" }}>
          Forgot Password?
        </Link>
      </>
    );
  };

  return (
    <BaseForm
      validate={validate}
      submitButtonText={"Login"}
      submittingButtonText={"Logging In"}
      defaultValues={{
        email: "",
        password: "",
      }}
      onSubmit={onSubmit}
      renderFields={fields}
      submitButtonStyle={{ width: "100%" }}
    />
  );
};
