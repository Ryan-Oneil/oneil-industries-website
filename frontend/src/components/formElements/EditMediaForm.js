import React from "react";
import { useDispatch } from "react-redux";
import { InputWithErrors, SelectInputWithErrors } from "./index";
import { Field, Formik } from "formik";
import { Alert, Button, Select } from "antd";
import {
  updateMedia,
  updateMediasLinkStatus
} from "../../reducers/mediaReducer";
import { getApiFormError, handleFormError } from "../../apis/ApiErrorHandler";
const { Option } = Select;

export default props => {
  const dispatch = useDispatch();
  const { id, name, linkStatus, publicMediaApproval } = props.media;
  const status = publicMediaApproval ? "Pending public approval" : linkStatus;

  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    return dispatch(updateMedia(formValues, id)).catch(error =>
      handleFormError(error, setFieldError, setStatus)
    );
  };

  const validate = values => {
    const errors = {};

    if (!values.name) {
      errors.name = "Name is required";
    }
    return errors;
  };

  return (
    <Formik
      initialValues={{
        name,
        privacy: status
      }}
      onSubmit={onSubmit}
      validate={validate}
      enableReinitialize={true}
    >
      {props => {
        const {
          isSubmitting,
          handleSubmit,
          isValid,
          errors,
          status,
          setStatus,
          setFieldValue,
          setFieldError
        } = props;
        return (
          <form onSubmit={handleSubmit} className="login-form">
            <Field
              name="name"
              as={InputWithErrors}
              type="text"
              placeholder="Media Name"
              error={errors.name}
            />
            <Field
              name="privacy"
              as={SelectInputWithErrors}
              type="privacy"
              onChange={data =>
                dispatch(updateMediasLinkStatus([id], data))
                  .then(status => {
                    if (status) {
                      setStatus({ type: "info", msg: status });
                    }
                    setFieldValue("privacy", data);
                  })
                  .catch(error =>
                    handleFormError(error, setFieldError, setStatus)
                  )
              }
            >
              <Option value="unlisted">Unlisted</Option>
              <Option value="public">Public</Option>
            </Field>
            <Button
              type="primary"
              htmlType="submit"
              className="formattedBackground"
              disabled={!isValid || isSubmitting}
              loading={isSubmitting}
              size="large"
            >
              {isSubmitting ? "Updating" : "Update"}
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
