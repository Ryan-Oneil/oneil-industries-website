import React from "react";
import { InputWithErrors, SelectInputWithErrors } from "./index";
import { useDispatch } from "react-redux";
import { updateUserQuota } from "../../reducers/adminReducer";
import { Field } from "formik";
import { Card, Select } from "antd";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";
const { Option } = Select;

export default (props) => {
  const { username } = props;
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    return dispatch(updateUserQuota(username, formValues)).catch((error) =>
      handleFormError(error, setFieldError, setStatus)
    );
  };

  const validate = (formValues) => {
    const errors = {};

    if (!formValues.max) {
      errors.max = "You must enter a quota max amount";
    }
    return errors;
  };

  const fields = (errors, setFieldValue) => {
    return (
      <>
        Max Storage:
        <Field
          name="max"
          as={InputWithErrors}
          type="text"
          placeholder="Max Quota"
          error={errors.max}
        />
        Ignore Quota:
        <Field
          name="ignoreQuota"
          as={SelectInputWithErrors}
          error={errors.ignoreQuota}
          onChange={(data) => setFieldValue("ignoreQuota", data)}
        >
          <Option value={"true"}>True</Option>
          <Option value={"false"}>False</Option>
        </Field>
      </>
    );
  };

  return (
    <Card title="Quota Settings">
      <BaseForm
        renderFields={fields}
        onSubmit={onSubmit}
        defaultValues={{
          max: props.quota.max,
          ignoreQuota: props.quota.ignoreQuota.toString(),
          used: props.quota.used,
        }}
        enableReinitialize
        validate={validate}
      />
    </Card>
  );
};
