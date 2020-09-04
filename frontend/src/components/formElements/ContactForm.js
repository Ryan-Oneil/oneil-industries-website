import React from "react";
import { Field, Formik } from "formik";
import { InputWithErrors } from "./index";
import { Alert, Button } from "antd";
import { getApiError } from "../../helpers";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import LockOutlined from "@ant-design/icons/lib/icons/LockOutlined";
import { useDispatch } from "react-redux";

export default () => {
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus }) => {
    // return dispatch().catch(error =>
    //   setStatus(getApiError(error))
    // );
  };

  return (
    <Formik
      initialValues={{
        name: "",
        email: "",
        subject: "",
        message: ""
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
              name="name"
              as={InputWithErrors}
              type="text"
              placeholder="Name"
              prefix={<UserOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.name}
            />
            <Field
              name="email"
              as={InputWithErrors}
              type="email"
              placeholder="Email"
              prefix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.email}
            />
            <Field
              name="subject"
              as={InputWithErrors}
              type="text"
              placeholder="Subject"
              prefix={<UserOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.subject}
            />
            <Field
              name="message"
              as={InputWithErrors}
              type="textarea"
              placeholder="Message"
              prefix={<UserOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.message}
            />
            <Button
              type="primary"
              htmlType="submit"
              className="form-button"
              disabled={!isValid || isSubmitting}
              loading={isSubmitting}
              size="large"
            >
              {isSubmitting ? "Submit" : "Submitting"}
            </Button>
            {status && (
              <Alert
                message="Error sending contact information"
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

  if (!values.name) {
    errors.name = "Name is required";
  }
  if (!values.email) {
    errors.email = "Email is required";
  }
  if (!values.subject) {
    errors.subject = "Subject is required";
  }
  if (!values.message) {
    errors.message = "Message is required";
  }
  return errors;
};
