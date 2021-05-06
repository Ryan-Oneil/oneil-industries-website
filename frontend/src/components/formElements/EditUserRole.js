import React, { useEffect } from "react";
import { SelectInputWithErrors } from "./index";
import { useDispatch, useSelector } from "react-redux";
import { getApiError } from "../../helpers";
import { getRoles, updateUserRole } from "../../reducers/adminReducer";
import { Field, Formik } from "formik";
import { Alert, Button, Card, Select } from "antd";
const { Option } = Select;

export default props => {
  const { username } = props;
  const dispatch = useDispatch();
  const { roles } = useSelector(state => state.admin);
  const roleDropdown = roles.map(role => (
    <Option key={role.id} value={role.role}>
      {role.role.replace("ROLE_", "")}
    </Option>
  ));

  useEffect(() => {
    dispatch(getRoles());
  }, []);

  const onSubmit = (formValues, { setStatus }) => {
    return dispatch(updateUserRole(username, formValues.role)).catch(error =>
      setStatus(getApiError(error))
    );
  };
  return (
    <Card title="Role Settings">
      <Formik
        initialValues={{
          role: props.role
        }}
        enableReinitialize
        onSubmit={onSubmit}
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
            <form onSubmit={handleSubmit}>
              <Field
                name="role"
                as={SelectInputWithErrors}
                error={errors.role}
                onChange={data => setFieldValue("role", data)}
              >
                {roleDropdown}
              </Field>
              <Button
                type="primary"
                htmlType="submit"
                className="centerContent formattedBackground"
                disabled={!isValid || isSubmitting}
                loading={isSubmitting}
              >
                {isSubmitting ? "Updating" : "Update"}
              </Button>
              {status && (
                <Alert
                  message="Update Error"
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
    </Card>
  );
};
