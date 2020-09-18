import React from "react";
import { getApiError } from "../../helpers";
import { Field, Formik } from "formik";
import { InputWithErrors } from "./index";
import { Alert, Button } from "antd";
import LockOutlined from "@ant-design/icons/lib/icons/LockOutlined";

export default props => {
  const onSubmit = (formValues, { setStatus }) => {
    return props.action(formValues).catch(error => {
      setStatus(getApiError(error));
    });
  };

  return (
    <Formik
      initialValues={{
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
          <form onSubmit={handleSubmit}>
            <Field
              name="password"
              as={InputWithErrors}
              type="password"
              placeholder="Password"
              prefix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.password}
            />
            <Button
              type="primary"
              htmlType="submit"
              className="form-button"
              disabled={!isValid || isSubmitting}
              size="large"
              loading={isSubmitting}
            >
              Change Password
            </Button>
            {status && (
              <Alert
                message="Change password Error"
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

  if (!values.password) {
    errors.password = "Password is required";
  }
  return errors;
};
