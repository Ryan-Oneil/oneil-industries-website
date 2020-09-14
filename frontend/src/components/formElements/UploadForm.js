import React from "react";
import { getApiError } from "../../helpers";
import { uploadMedia } from "../../reducers/mediaReducer";
import { Field, Formik } from "formik";
import { SelectInputWithErrors } from "./index";
import { Alert, Button, Select } from "antd";
const { Option } = Select;

export default ({ mediaList, onSubmitSuccess }) => {
  const onSubmit = (formValues, { setStatus }) => {
    return uploadMedia("/gallery/upload", formValues, mediaList)
      .then(data => {
        onSubmitSuccess();
        setStatus({ msg: data, type: "success" });
      })
      .catch(error => setStatus(getApiError(error)));
  };

  return (
    <Formik
      onSubmit={onSubmit}
      validate={validate}
      initialValues={{ linkStatus: "unlisted" }}
    >
      {props => {
        const {
          isSubmitting,
          handleSubmit,
          isValid,
          errors,
          status,
          setStatus,
          setFieldValue
        } = props;

        return (
          <form onSubmit={handleSubmit} className="login-form">
            <Field
              name="linkStatus"
              as={SelectInputWithErrors}
              placeholder="Privacy Status"
              error={errors.linkStatus}
              onChange={data => setFieldValue("linkStatus", data)}
            >
              <Option value="unlisted">Unlisted</Option>
              <Option value="private">Private</Option>
              <Option value="public">Public</Option>
            </Field>
            <Button
              type="primary"
              htmlType="submit"
              className="form-button"
              disabled={!isValid || isSubmitting || !mediaList.length > 0}
              loading={isSubmitting}
              size="large"
            >
              {isSubmitting ? "Uploading" : "Upload"}
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

  if (!values.linkStatus) {
    errors.linkStatus = "linkStatus is required";
  }
  return errors;
};
