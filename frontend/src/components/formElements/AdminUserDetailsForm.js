import React from "react";
import { Field, reduxForm } from "redux-form";
import { renderInput } from "./index";
import { renderErrorMessage } from "../Message";
import { connect } from "react-redux";
import { getRoles, updateUserDetails } from "../../actions/admin";

class AdminUserDetailsForm extends React.Component {
  constructor(props) {
    super(props);
    this.props.getRoles("/admin/users/roles");
  }

  onSubmit = formValues => {
    const { details } = this.props.admin.user;

    return this.props.updateUserDetails(
      `/admin/user/${details.username}/update/details/`,
      formValues,
      details.username
    );
  };

  render() {
    const { pristine, submitting, error, handleSubmit } = this.props;
    const { details } = this.props.admin.user;

    const roleDropDowns = this.props.admin.roles.map(role => {
      return (
        <option key={role.id} value={role.role}>
          {role.role.replace("ROLE_", "")}
        </option>
      );
    });
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
        <label>Account status:</label>
        <Field name="enabled" component="select" className="field">
          <option value={details.enabled}>
            {details.enabled ? "Enable" : "Disable"}
          </option>
          {details.enabled ? (
            <option value="false">Disable</option>
          ) : (
            <option value="true">Enable</option>
          )}
        </Field>
        <label>User Role:</label>
        <Field name="role" component="select" className="field">
          {roleDropDowns}
        </Field>
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

const mapStateToProps = state => {
  return { admin: state.admin };
};

export default connect(mapStateToProps, { getRoles, updateUserDetails })(
  reduxForm({
    form: "adminUserDetailsForm",
    enableReinitialize: true,
    validate
  })(AdminUserDetailsForm)
);
