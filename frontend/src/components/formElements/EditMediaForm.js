import React from "react";
import { useDispatch } from "react-redux";
import { InputWithErrors, SelectInputWithErrors } from "./index";
import { getApiError } from "../../helpers";
import { Field, Formik } from "formik";
import { Alert, Button } from "antd";
import { updateMedia } from "../../reducers/mediaReducer";

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
            >
              <option value="unlisted">Unlisted</option>
              <option value="public">Public</option>
              <option value="private">Private</option>
            </Field>
            <Button
              type="primary"
              htmlType="submit"
              className="form-button"
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
