import React from "react";
import { Field } from "formik";
import { InputWithErrors } from "./index";
import { useDispatch } from "react-redux";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";
import { updateUser } from "../../reducers/adminReducer";

export default ({ user, loading, style }) => {
  const dispatch = useDispatch();
  const { name, email } = user;

  const validate = (values) => {
    const errors = {};
    if (values.userEmail && values.userEmail.length > 255) {
      errors.userEmail = "Max length is 255 characters";
    }
    if (!values.userEmail) {
      errors.userEmail = "Email is required";
    }
    return errors;
  };

  const fields = () => {
    return (
      <>
        <Field
          name="Username"
          as={InputWithErrors}
          value={name}
          type="text"
          disabled
        />
        <Field
          name="userEmail"
          as={InputWithErrors}
          type="email"
          label="Email"
          disabled={loading}
          data-lpignore="true"
        />
        <Field
          name="userPass"
          as={InputWithErrors}
          type="password"
          label="Password"
          disabled={loading}
          data-lpignore="true"
        />
      </>
    );
  };

  return (
    // <Card title={loading ? "Loading User..." : `${name} Settings`}>
    <BaseForm
      submittingButtonText={"Saving..."}
      submitButtonText={"Save"}
      validate={validate}
      defaultValues={{
        username: name,
        userEmail: email,
      }}
      onSubmit={(values, { setStatus, setFieldError }) => {
        return dispatch(updateUser(values)).catch((error) =>
          handleFormError(error, setFieldError, setStatus)
        );
      }}
      enableReinitialize
      renderFields={fields}
      style={style}
    />
    // </Card>
  );
};
