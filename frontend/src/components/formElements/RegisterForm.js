import React from 'react';
import { Field, reduxForm } from 'redux-form';
import logo from '../../assets/images/oi-logo.png';
import '../../assets/css/layout.css';
import {renderIconInput} from "../formElements";
import {connect} from "react-redux";
import {registerUser} from "../../actions";
import {renderErrorMessage, renderPositiveMessage} from "../Message";

class RegisterForm extends React.Component {

    onSubmit = (formValues) => {
        const creds = { name: formValues.name.trim(), password: formValues.password.trim(), email : formValues.email.trim()};

        return this.props.registerUser(creds);
    };

    render() {
        const { submitting, changeState } = this.props;
        const { errorMessage, message, isRegistered } = this.props.auth;

        return (
            <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                <div className="ui segment">
                    <img src={logo} alt="Login Logo" className="ui centered small image"/>
                    <h1 className="textColorScheme">
                        Register an account
                    </h1>
                    <Field
                        name="name"
                        component={renderIconInput}
                        label="Enter Username"
                        iconType= "user"
                        type="text"
                    />
                    <Field
                        name="password"
                        component={renderIconInput}
                        label="Enter Password"
                        iconType= "lock"
                        type="password"
                    />
                    <Field
                        name="email"
                        component={renderIconInput}
                        label="Enter Email"
                        iconType= "envelope"
                        type="email"
                    />
                    {errorMessage && renderErrorMessage(errorMessage)}
                    {message && renderPositiveMessage(message)}
                    <button className="ui fluid large navColor submit button" disabled={submitting || isRegistered}>Register</button>
                    <p>Already have an account?  <button className="buttonLink" onClick={changeState}>Login</button></p>
                </div>
            </form>
        );
    }
}

const validate = (formValues) => {
    const errors = {};

    if (!formValues.name) {
        errors.name = "You must enter a Username";
    }
    if (!formValues.password) {
        errors.password = "You must enter a Password";
    }
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
    { registerUser }
)(reduxForm({ form: "register", enableReinitialize: true, validate })(RegisterForm));