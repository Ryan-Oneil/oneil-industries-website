import React from "react";
import { Card } from "antd";
import { connect } from "react-redux";
import { changePassword } from "../../reducers/authReducer";
import ChangePasswordForm from "../../components/formElements/ChangePasswordForm";

const ResetPassword = props => {
  let token = props.match.params.token;
  const { isAuthenticated } = props.auth;
  const { history } = props;

  if (!token || isAuthenticated) {
    history.push("/");
  }

  return (
    <div className="login">
      <Card>
        <ChangePasswordForm
          action={password =>
            changePassword(token, password).then(() => history.push("/login"))
          }
        />
      </Card>
    </div>
  );
};
const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(ResetPassword);
