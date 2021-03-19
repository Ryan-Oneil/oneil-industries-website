import React from "react";
import { getApiError } from "../../helpers";
import { postNewAlbum, uploadMedia } from "../../reducers/mediaReducer";
import { Field, Formik } from "formik";
import { SelectInputWithErrors } from "./index";
import { Alert, Button, Select } from "antd";
import SelectWithDropDown from "./SelectWithDropDown";
import { useDispatch } from "react-redux";
const { Option } = Select;

export default ({
  mediaList,
  onSubmitSuccess,
  albums,
  updateUploadProgress
}) => {
  const onSubmit = (formValues, { setStatus }) => {
    const uploadProgress = event => {
      const total = parseFloat(event.total);
      const current = event.loaded;

      let percentCompleted = Math.floor((current / total) * 100);
      updateUploadProgress(percentCompleted);
    };

    return uploadMedia("/gallery/upload", formValues, mediaList, uploadProgress)
      .then(data => {
        onSubmitSuccess();
        setStatus({ msg: data, type: "success" });
      })
      .catch(error => setStatus(getApiError(error)));
  };
  const dispatch = useDispatch();

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
              <Option value="public">Public</Option>
            </Field>
            <SelectWithDropDown
              optionValues={albums}
              placeHolder={"Album"}
              onChange={albumId => setFieldValue("albumId", albumId)}
              onSubmit={value => dispatch(postNewAlbum(value))}
            />
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
