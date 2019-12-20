import React from "react";
import { connect } from "react-redux";
import { Field, reduxForm } from "redux-form";
import logo from "../../assets/images/oi-logo.png";
import { renderIconInput } from "./index";
import { renderErrorMessage } from "../Message";
import { loginUser } from "../../actions";

class LoginForm extends React.Component {
  onSubmit = formValues => {
    const creds = {
      username: formValues.username.trim(),
      password: formValues.password.trim()
    };

    return this.props.loginUser(creds);
  };

  render() {
    const { submitting, signUp, resetPassword, error } = this.props;

    return (
      <form
        onSubmit={this.props.handleSubmit(this.onSubmit)}
        className="ui form error marginPadding"
      >
        <div className="ui segment">
          <img
            src={logo}
            alt="Login Logo"
            className="ui centered small image"
          />
          <h1 className="textColorScheme">Log-in to your account</h1>
          <Field
            name="username"
            component={renderIconInput}
            label="Enter Username"
            iconType="user"
            type="text"
          />
          <Field
            name="password"
            component={renderIconInput}
            label="Enter Password"
            iconType="lock"
            type="password"
          />

          {error && renderErrorMessage(error)}
          <button
            className="ui fluid large navColor submit button"
            disabled={submitting}
          >
            Login
          </button>
          <p>
            Don't have an account?{" "}
            <button className="buttonLink" onClick={() => signUp()}>
              Sign up
            </button>
          </p>
          <p>
            Forgot your password?{" "}
            <button className="buttonLink" onClick={() => resetPassword()}>
              Reset Password
            </button>
          </p>
        </div>
      </form>
    );
  }
}
const validate = formValues => {
  const errors = {};

  if (!formValues.username) {
    errors.username = "You must enter a Username";
  }
  if (!formValues.password) {
    errors.password = "You must enter a Password";
  }
  return errors;
};

const mapStateToProps = state => {
  return { auth: state.auth };
};

export default connect(mapStateToProps, { loginUser })(
  reduxForm({ form: "LoginForm", validate })(LoginForm)
);
