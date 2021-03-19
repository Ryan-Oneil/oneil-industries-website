import React from "react";
import { useDispatch } from "react-redux";
import { InputWithErrors, SelectInputWithErrors } from "./index";
import { getApiError } from "../../helpers";
import { Field, Formik } from "formik";
import { Alert, Button, Select } from "antd";
import { updateMedia } from "../../reducers/mediaReducer";
const { Option } = Select;

export default props => {
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus }) => {
    return dispatch(updateMedia(formValues, props.media.id)).catch(error =>
      setStatus(getApiError(error))
    );
  };

  return (
    <Formik
      initialValues={{
        name: props.media.name,
        privacy: props.media.linkStatus
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
          setFieldValue
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
              placeholder={"Privacy Status"}
              onChange={data => setFieldValue("privacy", data)}
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
                message="Media Update Error"
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
  return errors;
};
