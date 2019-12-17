import React from "react";
import { Field, reduxForm } from 'redux-form'
import {renderInput} from "./index";
import {renderErrorMessage, renderPositiveMessage} from "../Message";
import {connect} from "react-redux";
import {getRoles, updateUserDetails} from "../../actions/admin";

class AdminUserDetailsForm extends React.Component {

    constructor(props) {
        super(props);
        this.props.getRoles("/admin/roles");
    }

    onSubmit = (formValues) => {
        const { userDetails, closeModal } = this.props;

        closeModal();
        return this.props.updateUserDetails("/admin/updateUser/", formValues, userDetails.username);
    };

    render() {
        const { pristine, submitting, userDetails } = this.props;

        const roleDropDowns = this.props.admin.roles.map(role => {
            return <option key={role.id} value={role.role}>{role.role}</option>;
        });
        return (
            <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                <label>Email</label>
                <Field name="email"
                       component={renderInput}
                       type="email"
                       label="Email"
                />
                <label>Password</label>
                <Field name="password"
                       component={renderInput}
                       type="password"
                       label="Password"
                />
                <label>Account status:</label>
                <Field name="enabled" component="select" className="field">
                    <option value={userDetails.enabled}>{userDetails.enabled ? "Enable" : "Disable"}</option>
                    {userDetails.enabled ? <option value="false">Disable</option> : <option value="true">Enable</option>}
                </Field>
                <label>User Role:</label>
                <Field name="role" component="select" className="field">
                    {roleDropDowns}
                </Field>
                <button className="ui fluid large navColor submit button" disabled={submitting || pristine}>Update
                    Account
                </button>
                {this.props.errorMessage && renderErrorMessage(this.props.errorMessage)}
                {this.props.message && renderPositiveMessage(this.props.message)}
            </form>
        )
    }
}

const validate = (formValues) => {
    const errors = {};

    if (!formValues.email) {
        errors.email = "You must enter a Email";
    }
    return errors;
};

const mapStateToProps = (state) => {
    return {admin: state.admin};
};

export default connect(
    mapStateToProps,
    { getRoles,
        updateUserDetails}
)(reduxForm({ form: "adminUserDetailsForm", enableReinitialize: true, validate })(AdminUserDetailsForm));