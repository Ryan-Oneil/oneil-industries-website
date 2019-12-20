import React, { Fragment } from "react";
import { Redirect } from "react-router-dom";
import { connect } from "react-redux";

const PrivateRoute = props => {
  const { isAuthenticated } = props.auth;

  return (
    <Fragment>
      {isAuthenticated ? props.children : <Redirect to="/login" />}
    </Fragment>
  );
};

const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(PrivateRoute);
