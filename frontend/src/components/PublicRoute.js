import React, { Fragment } from "react";
import { Redirect } from "react-router-dom";
import { connect } from "react-redux";

const PublicRoute = props => {
  const { isAuthenticated } = props.auth;

  return (
    <Fragment>
      {isAuthenticated ? <Redirect to="/" /> : props.children}
    </Fragment>
  );
};

const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(PublicRoute);
