import React from "react";
import { Field } from "formik";
import { InputWithErrors } from "./index";
import { Card } from "antd";
import { useDispatch } from "react-redux";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";
import { updateUser } from "../../reducers/adminReducer";

export default ({ user, loading }) => {
  const dispatch = useDispatch();
  const { name, email } = user;

  const validate = values => {
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
          placeholder="username"
          disabled
        />
        <Field
          name="userEmail"
          as={InputWithErrors}
          type="email"
          placeholder="Email"
          disabled={loading}
        />
        <Field
          name="userPass"
          as={InputWithErrors}
          type="password"
          placeholder="password"
          disabled={loading}
        />
      </>
    );
  };

  return (
    <Card title={loading ? "Loading User..." : `${name} Settings`}>
      <BaseForm
        submittingButtonText={"Saving..."}
        submitButtonText={"Save"}
        validate={validate}
        defaultValues={{
          username: name,
          userEmail: email
        }}
        onSubmit={(values, { setStatus, setFieldError }) => {
          return dispatch(updateUser(values)).catch(error =>
            handleFormError(error, setFieldError, setStatus)
          );
        }}
        enableReinitialize
        renderFields={fields}
      />
    </Card>
  );
};
