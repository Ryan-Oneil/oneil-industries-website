import React from "react";
import { Field, reduxForm } from "redux-form";
import logo from "../../assets/images/oi-logo.png";
import "../../assets/css/layout.css";
import { renderIconInput } from "../formElements";
import { connect } from "react-redux";
import { renderErrorMessage, renderPositiveMessage } from "../Message";
import { changePassword } from "../../actions";

class NewPasswordForm extends React.Component {
  onSubmit = formValues => {
    let token = this.props.match.params.token;

    return this.props.changePassword(token, formValues.password.trim());
  };

  componentDidMount() {
    let token = this.props.match.params.token;

    if (this.props.auth.passwordReset) {
      this.props.history.push("/login");
    }

    if (!token || this.props.auth.isAuthenticated) {
      this.props.history.push("/");
    }
  }

  render() {
    const { submitting, error } = this.props;
    const { message } = this.props.auth;

    return (
      <div className="ui one column stackable center aligned page grid marginPadding">
        <div className="column twelve wide">
          <form
            onSubmit={this.props.handleSubmit(this.onSubmit)}
            className="ui form error"
          >
            <div className="ui segment">
              <img
                src={logo}
                alt="Login Logo"
                className="ui centered small image"
              />
              <h1 className="textColorScheme">New Password</h1>
              <Field
                name="password"
                component={renderIconInput}
                label="Enter new Password"
                iconType="lock"
                type="password"
              />
              {error && renderErrorMessage(error)}
              {message && renderPositiveMessage(message)}
              <button
                className="ui fluid large navColor submit button"
                disabled={submitting}
              >
                Confirm
              </button>
            </div>
          </form>
        </div>
      </div>
    );
  }
}

const validate = formValues => {
  const errors = {};

  if (!formValues.password) {
    errors.password = "You must enter a Password";
  }
  return errors;
};

const mapStateToProps = state => {
  return { auth: state.auth };
};

export default connect(mapStateToProps, { changePassword })(
  reduxForm({ form: "newPassword", enableReinitialize: true, validate })(
    NewPasswordForm
  )
);
