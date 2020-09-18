import React from "react";
import { Field, withFormik } from "formik";
import { ErrorDisplay, InputWithErrors } from "./index";
import { Alert, Button, Card, DatePicker } from "antd";
import moment from "moment";
import { getApiError } from "../../helpers";

const LinkForm = props => {
  const {
    isValid,
    isSubmitting,
    setFieldValue,
    touched,
    handleSubmit,
    values,
    status,
    setStatus
  } = props;

  const onDateChange = date => {
    setFieldValue("expires", date);
    touched.expires = true;
  };

  return (
    <Card>
      <form onSubmit={handleSubmit}>
        <Field
          name="title"
          as={InputWithErrors}
          type="text"
          placeholder="Link Title"
        />
        <DatePicker
          showTime={{ format: "HH:mm" }}
          onChange={onDateChange}
          style={{ width: "100%" }}
          placeholder="Link expiry date/time"
          size="large"
          disabledDate={current => {
            return current && current.valueOf() < Date.now();
          }}
          showToday={false}
          value={values.expires !== "" ? moment(values.expires) : ""}
        />
        <ErrorDisplay name="expires" />
        <Button
          type="primary"
          htmlType="submit"
          className="form-button"
          disabled={!isValid || isSubmitting}
          style={{ marginTop: 24 }}
          loading={isSubmitting}
        >
          Confirm
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
    </Card>
  );
};

export const EditLinkForm = withFormik({
  mapPropsToValues: props => ({
    title: props.link.title,
    expires: moment(props.link.expiryDatetime)
  }),
  validate: values => {
    const errors = {};

    if (values.title && values.title.length > 255) {
      errors.title = "Max length is 255 characters";
    }
    if (!values.expires) {
      errors.expires = "Expiry is required";
    }
    if (Date.parse(values.expires) < Date.now()) {
      errors.expires = "Expiry date/time has already happened";
    }
    return errors;
  },
  handleSubmit: (values, { setStatus, props }) => {
    const updatedLink = {
      title: values.title,
      expires: values.expires.toISOString().replace(/\.[0-9]{3}/, "")
    };
    return props
      .submitAction(props.id, updatedLink)
      .catch(error => setStatus({ msg: getApiError(error), type: "error" }));
  },
  validateOnMount: true
})(LinkForm);
