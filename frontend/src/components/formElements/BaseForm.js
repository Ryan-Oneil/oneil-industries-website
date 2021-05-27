import { Formik } from "formik";
import { Alert, Button } from "antd";
import React from "react";
import SaveOutlined from "@ant-design/icons/lib/icons/SaveOutlined";

export default ({
  renderFields,
  defaultValues,
  onSubmit,
  validate,
  submitButtonText = "Update",
  submittingButtonText = "Updating",
  enableReinitialize,
  submitButtonStyle,
  style,
}) => {
  return (
    <Formik
      initialValues={defaultValues}
      onSubmit={onSubmit}
      validate={validate}
      enableReinitialize={enableReinitialize}
      validateOnMount
    >
      {(props) => {
        const {
          isSubmitting,
          handleSubmit,
          isValid,
          errors,
          setFieldValue,
          setFieldError,
          status,
          setStatus,
          values,
        } = props;

        return (
          <form onSubmit={handleSubmit} style={style}>
            {renderFields(
              errors,
              setFieldValue,
              setFieldError,
              setStatus,
              values
            )}
            <Button
              type="primary"
              htmlType="submit"
              className="formattedBackground centerContent"
              disabled={!isValid || isSubmitting}
              loading={isSubmitting}
              icon={<SaveOutlined />}
              size="large"
              style={{
                width: "50%",
                borderRadius: ".375rem",
                ...submitButtonStyle,
              }}
            >
              {isSubmitting ? submittingButtonText : submitButtonText}
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
  );
};
