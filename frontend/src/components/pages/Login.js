import React from 'react';
import { Field, reduxForm } from 'redux-form';
import logo from '../../assets/images/oi-logo.png';
import {loginUser} from "../../actions";
import '../../assets/css/layout.css';
import {renderIconInput} from "../formElements";

class Login extends React.Component {

    onSubmit = (formValues) => {
        const dispatch = this.props.dispatch;
        const creds = { username: formValues.username.trim(), password: formValues.password.trim()};

        return dispatch(loginUser(creds));
    };

    render() {
        //If user is already logged in redirect to their profile
        if (this.props.isAuthenticated) {
            this.props.history.push('/profile');
        }
        const { submitting, errorMessage } = this.props;

        return (
            <div className="ui one column stackable center aligned page grid">
                <div className="column twelve wide">
                    <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                        <div className="ui segment">
                                <img src={logo} alt="Login Logo" className="ui centered small image"/>
                                <h1 className="textColorScheme">
                                    Log-in to your account
                                </h1>
                            <Field
                                name="username"
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
                            {errorMessage && <div className="ui error message">
                                {<div className="header">{errorMessage}</div>}
                            </div>}
                            <button className="ui fluid large navColor submit button" disabled={submitting}>Login</button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}

const validate = (formValues) => {
    const errors = {};

    if (!formValues.username) {
        errors.username = "You must enter a Username";
    }
    if (!formValues.password) {
        errors.password = "You must enter a Password";
    }
    return errors;
};

export default reduxForm({
    form: 'login',
    validate
})(Login);