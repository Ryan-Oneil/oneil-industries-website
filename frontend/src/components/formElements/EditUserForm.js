import React from "react";
import { Field, withFormik } from "formik";
import { InputWithErrors } from "./index";
import { Alert, Button, Card } from "antd";
import { getApiError } from "../../helpers";
import { connect } from "react-redux";
import { updateUser } from "../../reducers/adminReducer";

const LinkForm = props => {
  const {
    isValid,
    isSubmitting,
    handleSubmit,
    status,
    setStatus,
    loading
  } = props;
  const { name } = props.user;

  return (
    <Card title={loading ? `Loading User...` : `${name} Settings`}>
      <form onSubmit={handleSubmit}>
        <Field
          name="Username"
          as={InputWithErrors}
          value={name}
          type="text"
          placeholder="username"
          disabled
        />
        <Field
          name="userEmail"
          as={InputWithErrors}
          type="email"
          placeholder="Email"
          disabled={loading}
        />
        <Field
          name="userPass"
          as={InputWithErrors}
          type="password"
          placeholder="password"
          disabled={loading}
        />
        <Button
          type="primary"
          htmlType="submit"
          className="form-button"
          disabled={!isValid || isSubmitting || loading}
          loading={isSubmitting}
        >
          {isSubmitting ? "Saving..." : "Save"}
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

const EditUserForm = withFormik({
  enableReinitialize: true,
  mapPropsToValues: props => ({
    username: props.user.name,
    userEmail: props.user.email
  }),
  validate: values => {
    const errors = {};
    if (values.userEmail && values.userEmail.length > 255) {
      errors.userEmail = "Max length is 255 characters";
    }
    if (!values.userEmail) {
      errors.userEmail = "Email is required";
    }
    return errors;
  },
  handleSubmit: (values, { props, setStatus }) => {
    return props
      .updateUser(values)
      .catch(error => setStatus({ msg: getApiError(error), type: "error" }));
  },
  validateOnMount: true
})(LinkForm);

export default connect(null, { updateUser })(EditUserForm);
