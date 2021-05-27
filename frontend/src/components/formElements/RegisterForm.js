import React from "react";
import { Field } from "formik";
import { InputWithErrors } from "./index";
import MailOutlined from "@ant-design/icons/lib/icons/MailOutlined";
import LockOutlined from "@ant-design/icons/lib/icons/LockOutlined";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import { registerUser } from "../../reducers/authReducer";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";

export default () => {
  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    const creds = {
      username: formValues.username.trim(),
      password: formValues.password.trim(),
      email: formValues.email.trim(),
    };
    return registerUser(creds)
      .then((response) => setStatus({ msg: response.data, type: "success" }))
      .catch((error) => handleFormError(error, setFieldError, setStatus));
  };

  const validate = (values) => {
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

  const fields = (errors) => {
    return (
      <>
        <Field
          name="username"
          as={InputWithErrors}
          type="text"
          suffix={<UserOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
          error={errors.username}
        />
        <Field
          name="password"
          as={InputWithErrors}
          type="password"
          suffix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
          error={errors.password}
        />
        <Field
          name="confirmPassword"
          as={InputWithErrors}
          type="password"
          label="Confirm Password"
          suffix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
          error={errors.confirmPassword}
        />
        <Field
          name="email"
          as={InputWithErrors}
          type="email"
          suffix={<MailOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
          error={errors.email}
        />
      </>
    );
  };

  return (
    <BaseForm
      renderFields={fields}
      onSubmit={onSubmit}
      defaultValues={{
        username: "",
        password: "",
        confirmPassword: "",
        email: "",
      }}
      submittingButtonText={"Registering"}
      submitButtonText={"Register"}
      validate={validate}
    />
  );
};
