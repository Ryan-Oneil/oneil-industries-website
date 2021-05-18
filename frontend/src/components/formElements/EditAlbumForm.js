import React from "react";
import { useDispatch } from "react-redux";
import { InputWithErrors } from "./index";
import { Field } from "formik";
import { updateAlbum } from "../../reducers/mediaReducer";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";

export default props => {
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    return dispatch(updateAlbum(formValues, props.album.id)).catch(error =>
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

  const fields = errors => {
    return (
      <Field
        name="name"
        as={InputWithErrors}
        type="text"
        placeholder="Album Name"
        error={errors.name}
      />
    );
  };

  return (
    <BaseForm
      onSubmit={onSubmit}
      defaultValues={{
        name: props.album.name
      }}
      validate={validate}
      renderFields={fields}
      submitButtonText={"Update"}
      submittingButtonText={"Updating"}
    />
  );
};
