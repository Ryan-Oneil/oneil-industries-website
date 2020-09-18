import React from "react";
import { Field, Formik } from "formik";
import { InputWithErrors } from "./index";
import { Alert, Button } from "antd";
import { getApiError } from "../../helpers";

import MailOutlined from "@ant-design/icons/lib/icons/MailOutlined";

export default props => {
  const onSubmit = (formValues, { setStatus }) => {
    return props
      .action(formValues)
      .then(response => {
        if (response) {
          setStatus({ msg: response.data, type: "success" });
        }
      })
      .catch(error => {
        setStatus({ msg: getApiError(error), type: "error" });
      });
  };

  return (
    <Formik
      initialValues={{
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
          <form onSubmit={handleSubmit}>
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
              className="form-button"
              disabled={!isValid || isSubmitting}
              size="large"
              loading={isSubmitting}
            >
              Confirm
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

const validate = values => {
  const errors = {};

  if (!values.email) {
    errors.email = "Email is required";
  }
  return errors;
};
