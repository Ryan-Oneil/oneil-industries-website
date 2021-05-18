import React from "react";
import { Field } from "formik";
import { InputWithErrors } from "./index";
import LockOutlined from "@ant-design/icons/lib/icons/LockOutlined";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";

export default props => {
  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    return props
      .action(formValues)
      .catch(error => handleFormError(error, setFieldError, setStatus));
  };

  const validate = values => {
    const errors = {};

    if (!values.password) {
      errors.password = "Password is required";
    }
    return errors;
  };

  const fields = errors => {
    return (
      <Field
        name="password"
        as={InputWithErrors}
        type="password"
        placeholder="Password"
        prefix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
        error={errors.password}
      />
    );
  };

  return (
    <BaseForm
      submittingButtonText={"Submitting..."}
      submitButtonText={"Change Password"}
      defaultValues={{ password: "" }}
      validate={validate}
      onSubmit={onSubmit}
      renderFields={fields}
    />
  );
};
