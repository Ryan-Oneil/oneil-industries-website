import React from "react";
import { Field, reduxForm } from "redux-form";
import { renderInput } from "./index";
import { renderErrorMessage } from "../Message";
import { connect } from "react-redux";
import { changeUserDetails } from "../../actions/profile";

class ChangeUserDetailsForm extends React.Component {
  onSubmit = formValues => {
    return this.props.changeUserDetails(formValues, "/user/profile/update");
  };

  render() {
    const { pristine, submitting, error, handleSubmit } = this.props;

    return (
      <form onSubmit={handleSubmit(this.onSubmit)} className="ui form error">
        <label>Email</label>
        <Field
          name="email"
          component={renderInput}
          type="email"
          label="Email"
        />
        <label>Password</label>
        <Field
          name="password"
          component={renderInput}
          type="password"
          label="Password"
        />
        {error && renderErrorMessage(error)}
        <button
          className="ui fluid large buttonFormat submit button"
          disabled={submitting || pristine}
        >
          Update Account
        </button>
      </form>
    );
  }
}

const validate = formValues => {
  const errors = {};

  if (!formValues.email) {
    errors.email = "You must enter a Email";
  }
  return errors;
};

export default connect(null, { changeUserDetails })(
  reduxForm({ form: "changeUserDetails", validate })(ChangeUserDetailsForm)
);
