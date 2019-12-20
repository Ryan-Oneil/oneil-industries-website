import React from "react";
import "../assets/css/layout.css";
import RegisterForm from "../components/formElements/RegisterForm";
import ResetPasswordForm from "../components/formElements/ResetPasswordForm";
import LoginForm from "../components/formElements/LoginForm";
import { connect } from "react-redux";

class Login extends React.Component {
  state = { showingForm: "login" };

  showLoginForm = () => {
    this.setState({ showingForm: "login" });
  };

  showRegisterForm = () => {
    this.setState({ showingForm: "register" });
  };

  showResetPasswordForm = () => {
    this.setState({ showingForm: "forgotPassword" });
  };

  renderForm = () => {
    switch (this.state.showingForm) {
      case "register": {
        return <RegisterForm changeState={this.showLoginForm} />;
      }
      case "forgotPassword": {
        return (
          <ResetPasswordForm
            login={this.showLoginForm}
            signUp={this.showRegisterForm}
          />
        );
      }
      default: {
        return (
          <LoginForm
            signUp={this.showRegisterForm}
            resetPassword={this.showResetPasswordForm}
          />
        );
      }
    }
  };

  render() {
    if (this.props.auth.isAuthenticated) {
      this.props.history.goBack();
    }
    return (
      <div className="ui one column stackable center aligned page grid">
        <div className="column twelve wide">{this.renderForm()}</div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(Login);
