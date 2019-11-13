import React from "react";
import { Field, reduxForm } from 'redux-form'
import {renderInput} from "./index";
import {renderErrorMessage, renderPositiveMessage} from "../Message";

const ChangeUserDetailsForm = props => {
    const { handleSubmit, pristine, submitting } = props;

    return (
        <form onSubmit={handleSubmit} className="ui form error">
            {props.children}
            <Field name="password"
                   component={renderInput}
                   type="password"
                   label="Password"
            />
            <button className="ui fluid large navColor submit button" disabled={submitting || pristine}>Update Account</button>
            {props.errorMessage && renderErrorMessage(props.errorMessage)}
            {props.message && renderPositiveMessage(props.message)}
        </form>
    )
};
export default reduxForm({
    form: 'changeUserDetails'
})(ChangeUserDetailsForm)