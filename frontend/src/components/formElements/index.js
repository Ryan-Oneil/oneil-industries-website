import React from "react";
import { ErrorMessage } from "formik";
import { Input, Select } from "antd";

export const InputLabel = ({ label, name }) => {
  return (
    <label
      htmlFor={name}
      style={{
        fontWeight: 500,
        color: "rgba(55,65,81,var(--tw-text-opacity))",
        textTransform: "capitalize",
      }}
    >
      {label ? label : name}
    </label>
  );
};

export const InputWithErrors = (props) => {
  const hasError = props.error ? "has-error" : "";

  return (
    <div className="inputField">
      <InputLabel label={props.label} name={props.name} />
      <Input
        {...props}
        size="large"
        className={`${hasError} roundedShadowBox`}
        style={{
          marginTop: ".25rem",
          padding: ".5rem .75rem",
        }}
      />
      <ErrorDisplay name={props.name} />
    </div>
  );
};

export const SelectInputWithErrors = (props) => {
  const hasError = props.error ? "has-error" : "";

  return (
    <div className="inputField">
      <InputLabel label={props.label} name={props.name} />
      <Select
        {...props}
        size="large"
        className={hasError}
        style={{ width: "100%", textAlign: "start" }}
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
