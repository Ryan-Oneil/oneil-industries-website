import React from "react";
import { useDispatch } from "react-redux";
import { InputWithErrors } from "./index";
import { getApiError } from "../../helpers";
import { Field, Formik } from "formik";
import { Alert, Button } from "antd";
import { updateAlbum } from "../../reducers/mediaReducer";

export default props => {
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus }) => {
    return dispatch(updateAlbum(formValues, props.album.id)).catch(error =>
      setStatus(getApiError(error))
    );
  };

  return (
    <Formik
      initialValues={{
        name: props.album.name
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
              placeholder="Album Name"
              error={errors.name}
            />

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
                message="Album Update Error"
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
