import React from "react";
import { ErrorMessage } from "formik";
import { Input, Select } from "antd";

export const InputWithErrors = props => {
  const hasError = props.error ? "has-error" : "";

  return (
    <div className="inputField">
      <Input {...props} size="large" className={hasError} />
      <ErrorDisplay name={props.name} />
    </div>
  );
};

export const SelectInputWithErrors = props => {
  const hasError = props.error ? "has-error" : "";

  return (
    <div className="inputField">
      <Select
        {...props}
        size="large"
        className={hasError}
        style={{ width: "100%" }}
      >
        {props.children}
      </Select>
      <ErrorDisplay name={props.name} />
    </div>
  );
};

export const ErrorDisplay = ({ name }) => {
  return (
    <ErrorMessage name={name} style={{ color: "#f5222d" }} component="span" />
  );
};
