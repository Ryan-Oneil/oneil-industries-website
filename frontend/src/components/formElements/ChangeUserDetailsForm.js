import React from "react";
import { Field, reduxForm } from 'redux-form'
import {renderInput} from "./index";
import {renderErrorMessage, renderPositiveMessage} from "../Message";

const ChangeUserDetailsForm = props => {
    const { handleSubmit, pristine, submitting } = props;

    return (
        <form onSubmit={handleSubmit} className="ui form error">
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
            {props.children}
            <button className="ui fluid large navColor submit button" disabled={submitting || pristine}>Update Account</button>
            {props.errorMessage && renderErrorMessage(props.errorMessage)}
            {props.message && renderPositiveMessage(props.message)}
        </form>
    )
};

const validate = (formValues) => {
    const errors = {};

    if (!formValues.email) {
        errors.email = "You must enter a Email";
    }
    return errors;
};

export default reduxForm({
    form: 'changeUserDetails',
    validate
})(ChangeUserDetailsForm)