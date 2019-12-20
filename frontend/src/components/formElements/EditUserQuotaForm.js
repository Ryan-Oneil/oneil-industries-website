import React from "react";
import { Field, reduxForm } from "redux-form";
import { renderInput } from "./index";
import { renderErrorMessage } from "../Message";
import { connect } from "react-redux";
import { getRoles, updateUserQuota } from "../../actions/admin";

class EditUserQuotaForm extends React.Component {
  onSubmit = formValues => {
    const { details } = this.props.admin.user;

    return this.props.updateUserQuota(
      `/admin/updateUserQuota/${details.username}`,
      formValues
    );
  };

  render() {
    const { pristine, submitting, error } = this.props;
    const { ignoreQuota } = this.props.admin.user.storageQuota;

    const option = ignoreQuota ? "false" : "true";
    return (
      <form
        onSubmit={this.props.handleSubmit(this.onSubmit)}
        className="ui form error"
      >
        <label>Max storage amount (In GBs):</label>
        <Field
          name="max"
          component={renderInput}
          type="number"
          label="Max storage amount"
        />
        <label>Ignore storage quota:</label>
        <Field name="ignoreQuota" component="select" className="field">
          <option value={ignoreQuota ? "true" : "false"}>
            {ignoreQuota ? "true" : "false"}
          </option>
          <option value={option}>{option}</option>
        </Field>
        {error && renderErrorMessage(error)}
        <button
          className="ui fluid large navColor submit button"
          disabled={submitting || pristine}
        >
          Update Quota
        </button>
      </form>
    );
  }
}
const validate = formValues => {
  const errors = {};

  if (!formValues.max) {
    errors.max = "You must enter a quota max amount";
  }
  return errors;
};

const mapStateToProps = state => {
  return { admin: state.admin };
};

export default connect(mapStateToProps, { getRoles, updateUserQuota })(
  reduxForm({ form: "EditUserQuotaForm", enableReinitialize: true, validate })(
    EditUserQuotaForm
  )
);
