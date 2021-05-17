import React, { Fragment } from "react";
import { Redirect } from "react-router-dom";
import { connect } from "react-redux";
import { HOME_URL } from "../constants/constants";

const PublicRoute = props => {
  const redirectBack =
    props.location.state && props.location.state.redirectBack;
  const { isAuthenticated } = props.auth;
  let redirectTo = HOME_URL;

  if (isAuthenticated && redirectBack) {
    redirectTo = props.location.state.redirectTo;
  }
  return (
    <Fragment>
      {isAuthenticated ? <Redirect to={redirectTo} /> : props.children}
    </Fragment>
  );
};

const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(PublicRoute);
