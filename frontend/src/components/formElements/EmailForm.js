import React from "react";
import { Field } from "formik";
import { InputWithErrors } from "./index";
import MailOutlined from "@ant-design/icons/lib/icons/MailOutlined";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";

export default props => {
  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    return props
      .action(formValues)
      .then(response => {
        if (response) {
          setStatus({ msg: response.data, type: "success" });
        }
      })
      .catch(error => handleFormError(error, setFieldError, setStatus));
  };

  const validate = values => {
    const errors = {};

    if (!values.email) {
      errors.email = "Email is required";
    }
    return errors;
  };

  const fields = errors => {
    return (
      <Field
        name="email"
        as={InputWithErrors}
        type="email"
        placeholder="Email"
        prefix={<MailOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
        error={errors.email}
      />
    );
  };

  return (
    <BaseForm
      renderFields={fields}
      onSubmit={onSubmit}
      defaultValues={{
        email: ""
      }}
      submittingButtonText={"Confirming..."}
      submitButtonText={"Confirm"}
      validate={validate}
    />
  );
};
