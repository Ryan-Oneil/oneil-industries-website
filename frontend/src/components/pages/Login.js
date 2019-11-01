import React from 'react';
import { Field, reduxForm } from 'redux-form';
import logo from '../../assets/images/oi-logo.png';
import {loginUser} from "../../actions";
import '../../assets/css/layout.css';
import {renderIconInput} from "../formElements";
import RegisterForm from "../formElements/RegisterForm";
import ResetPasswordForm from "../formElements/ResetPasswordForm";

class Login extends React.Component {

    state = { showingForm: "login"};

    onSubmit = (formValues) => {
        const dispatch = this.props.dispatch;
        const creds = { username: formValues.username.trim(), password: formValues.password.trim()};

        return dispatch(loginUser(creds));
    };

    showLoginForm = () => {
        this.setState({showingForm: "login"});
    };

    showRegisterForm = () => {
        this.setState({showingForm: "register"});
    };

    renderLoginForm = ({errorMessage, submitting}) => {
        return (
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
                    <p>Don't have an account? <button className="buttonLink" onClick={this.showRegisterForm}>Sign up</button></p>
                    <p>Forgot your password? <button className="buttonLink" onClick={()=> {this.setState({showingForm: "forgotPassword"})}}>Reset Password</button></p>
                </div>
            </form>

        )
    };

    renderForm = () => {
        const { submitting, errorMessage } = this.props;

        switch (this.state.showingForm) {
            case "register": {
                return <RegisterForm changeState={this.showLoginForm}/>;
            }
            case "forgotPassword": {
                return <ResetPasswordForm login={this.showLoginForm} signUp={this.showRegisterForm}/>
            }
            default: {
                return this.renderLoginForm({ submitting, errorMessage });
            }
        }
    };

    render() {
        if (this.props.isAuthenticated) {
            this.props.history.goBack();
        }
        return (
            <div className="ui one column stackable center aligned page grid">
                <div className="column twelve wide">
                    {this.renderForm()}
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