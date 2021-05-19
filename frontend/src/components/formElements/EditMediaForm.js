import React from "react";
import { useDispatch } from "react-redux";
import { InputWithErrors, SelectInputWithErrors } from "./index";
import { Field } from "formik";
import { Select } from "antd";
import {
  updateMedia,
  updateMediasLinkStatus,
} from "../../reducers/mediaReducer";
import { handleFormError } from "../../apis/ApiErrorHandler";
import BaseForm from "./BaseForm";
const { Option } = Select;

export default (props) => {
  const dispatch = useDispatch();
  const { id, name, linkStatus, publicMediaApproval } = props.media;

  const onSubmit = (formValues, { setStatus, setFieldError }) => {
    return dispatch(updateMedia(formValues, id)).catch((error) =>
      handleFormError(error, setFieldError, setStatus)
    );
  };

  const validate = (values) => {
    const errors = {};

    if (!values.name) {
      errors.name = "Name is required";
    }
    return errors;
  };

  const fields = (errors, setFieldValue, setFieldError, setStatus) => {
    return (
      <>
        <Field
          name="name"
          as={InputWithErrors}
          type="text"
          placeholder="Media Name"
          error={errors.name}
        />
        <Field
          name="privacy"
          as={SelectInputWithErrors}
          type="privacy"
          onChange={(data) =>
            dispatch(updateMediasLinkStatus([id], data))
              .then((status) => {
                if (status) {
                  setStatus({ type: "info", msg: status });
                } else {
                  setStatus("");
                }
                setFieldValue("privacy", data);
              })
              .catch((error) =>
                handleFormError(error, setFieldError, setStatus)
              )
          }
        >
          <Option value="unlisted">Unlisted</Option>
          <Option value="public">Public</Option>
        </Field>
      </>
    );
  };

  return (
    <BaseForm
      onSubmit={onSubmit}
      defaultValues={{
        name,
        privacy: publicMediaApproval ? "Pending public approval" : linkStatus,
      }}
      validate={validate}
      renderFields={fields}
      submitButtonText={"Update"}
      submittingButtonText={"Updating"}
    />
  );
};
