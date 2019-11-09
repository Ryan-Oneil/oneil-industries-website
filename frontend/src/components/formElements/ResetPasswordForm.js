import React from 'react';
import { Field, reduxForm } from 'redux-form';
import logo from '../../assets/images/oi-logo.png';
import '../../assets/css/layout.css';
import {renderIconInput} from "../formElements";
import {connect} from "react-redux";
import {resetPassword} from "../../actions";
import {renderErrorMessage, renderPositiveMessage} from "../Message";

class ResetPasswordForm extends React.Component {

    onSubmit = (formValues) => {
        const email = formValues.email.trim();

        return this.props.resetPassword(email);
    };

    render() {
        const { submitting, login, signUp } = this.props;
        const { errorMessage, message, hasSentResetEmail } = this.props.auth;

        return (
            <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                <div className="ui segment">
                    <img src={logo} alt="Login Logo" className="ui centered small image"/>
                    <h1 className="textColorScheme">
                        Reset Password
                    </h1>
                    <Field
                        name="email"
                        component={renderIconInput}
                        label="Enter Email"
                        iconType= "envelope"
                        type="email"
                    />
                    {errorMessage && renderErrorMessage(errorMessage)}
                    {message && renderPositiveMessage(message)}
                    <button className="ui fluid large navColor submit button" disabled={submitting || hasSentResetEmail}>Reset</button>
                    <p>Remembered your password?  <button className="buttonLink" onClick={login}>Login</button></p>
                    <p>Don't have an account?  <button className="buttonLink" onClick={signUp}>Sign up</button></p>
                </div>
            </form>
        );
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
    return {auth: state.auth};
};

export default connect(
    mapStateToProps,
    { resetPassword }
)(reduxForm({ form: "resetPassword", enableReinitialize: true, validate })(ResetPasswordForm));