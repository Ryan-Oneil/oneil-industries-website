import React from "react";
import { Field } from "formik";
import { ErrorDisplay, InputWithErrors } from "./index";
import { Card, DatePicker } from "antd";
import moment from "moment";
import BaseForm from "./BaseForm";

export default ({
  submitAction,
  title = "",
  expires,
  submitButtonText,
  submittingButtonText
}) => {
  const fields = (errors, setFieldValue, setFieldError, setStatus, values) => {
    return (
      <>
        <Field
          name="title"
          as={InputWithErrors}
          type="text"
          placeholder="Link Title"
        />
        <DatePicker
          showTime={{ format: "HH:mm" }}
          onChange={date => {
            setFieldValue("expires", date);
          }}
          style={{ width: "100%", marginBottom: 24 }}
          placeholder="Link expiry date/time"
          size="large"
          disabledDate={current => {
            return current && current.valueOf() < Date.now();
          }}
          showToday={false}
          value={values.expires !== "" ? moment(values.expires) : ""}
        />
        <ErrorDisplay name="expires" />
      </>
    );
  };

  return (
    <Card className={"roundedShadowBox"}>
      <BaseForm
        enableReinitialize
        renderFields={fields}
        submitButtonText={submitButtonText}
        submittingButtonText={submittingButtonText}
        validate={values => {
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
        }}
        defaultValues={{
          title,
          expires
        }}
        onSubmit={values => {
          let params = {
            title: values.title,
            expires: values.expires.toISOString().replace(/\.[0-9]{3}/, "")
          };
          return submitAction(params);
        }}
      />
    </Card>
  );
};
