import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { USER_ALBUMS_ENDPOINT } from "../../apis/endpoints";
import { getApiError } from "../../helpers";
import { uploadMedia } from "../../reducers/mediaReducer";
import { Field, Formik } from "formik";
import { SelectInputWithErrors } from "./index";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import { Alert, Button, Select } from "antd";
const { Option } = Select;

export default ({ mediaList, onSubmitSuccess }) => {
  const dispatch = useDispatch();

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
      initialValues={{
        linkStatus: "unlisted"
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
              name="linkStatus"
              as={SelectInputWithErrors}
              type="text"
              placeholder="Privacy Status"
              prefix={<UserOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              error={errors.linkStatus}
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
