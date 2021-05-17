import React, { Fragment } from "react";
import { Redirect } from "react-router-dom";
import { connect } from "react-redux";
import { LOGIN_URL } from "../constants/constants";

const PrivateRoute = props => {
  const { isAuthenticated } = props.auth;
  const { pathname } = props.location;

  return (
    <Fragment>
      {isAuthenticated ? (
        props.children
      ) : (
        <Redirect
          to={{
            pathname: LOGIN_URL,
            state: { redirectBack: true, redirectTo: pathname }
          }}
        />
      )}
    </Fragment>
  );
};

const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(PrivateRoute);
