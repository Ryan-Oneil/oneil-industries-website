import React from "react";
import { InputWithErrors, SelectInputWithErrors } from "./index";
import { useDispatch } from "react-redux";
import { updateUserQuota } from "../../reducers/adminReducer";
import { Field, Formik } from "formik";
import { Alert, Button, Card, Select } from "antd";
import { handleFormError } from "../../apis/ApiErrorHandler";
const { Option } = Select;

export default props => {
  const { username } = props;
  const dispatch = useDispatch();

  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    return dispatch(updateUserQuota(username, formValues)).catch(error =>
      handleFormError(error, setFieldError, setStatus)
    );
  };

  const validate = formValues => {
    const errors = {};

    if (!formValues.max) {
      errors.max = "You must enter a quota max amount";
    }
    return errors;
  };

  return (
    <Card title="Quota Settings">
      <Formik
        initialValues={{
          max: props.quota.max,
          ignoreQuota: props.quota.ignoreQuota.toString(),
          used: props.quota.used
        }}
        enableReinitialize
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
            setStatus,
            setFieldValue
          } = props;

          return (
            <form onSubmit={handleSubmit}>
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
                onChange={data => setFieldValue("ignoreQuota", data)}
              >
                <Option value={"true"}>True</Option>
                <Option value={"false"}>False</Option>
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
    </Card>
  );
};
