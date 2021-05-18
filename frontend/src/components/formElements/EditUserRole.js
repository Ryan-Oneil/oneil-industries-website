import React, { useEffect } from "react";
import { SelectInputWithErrors } from "./index";
import { useDispatch, useSelector } from "react-redux";
import { getRoles, updateUserRole } from "../../reducers/adminReducer";
import { Field } from "formik";
import { Card, Select } from "antd";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";
const { Option } = Select;

export default props => {
  const { username } = props;
  const dispatch = useDispatch();
  const { roles } = useSelector(state => state.admin);

  useEffect(() => {
    dispatch(getRoles());
  }, []);

  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    return dispatch(updateUserRole(username, formValues.role)).catch(error =>
      handleFormError(error, setFieldError, setStatus)
    );
  };

  const fields = (errors, setFieldValue) => {
    return (
      <Field
        name="role"
        as={SelectInputWithErrors}
        error={errors.role}
        onChange={data => setFieldValue("role", data)}
      >
        {roles.map(role => (
          <Option key={role.id} value={role.role}>
            {role.role.replace("ROLE_", "")}
          </Option>
        ))}
      </Field>
    );
  };

  return (
    <Card title="Role Settings">
      <BaseForm
        defaultValues={{
          role: props.role
        }}
        enableReinitialize
        onSubmit={onSubmit}
        renderFields={fields}
      />
    </Card>
  );
};
