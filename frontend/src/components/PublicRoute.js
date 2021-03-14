import React, { Fragment } from "react";
import { Redirect, useHistory } from "react-router-dom";
import { connect } from "react-redux";
import { HOME_URL } from "../constants/constants";

const PublicRoute = props => {
  let history = useHistory();
  const redirectBack =
    props.location.state && props.location.state.redirectBack;
  const { isAuthenticated } = props.auth;

  if (isAuthenticated && redirectBack) {
    history.goBack();
  }
  return (
    <Fragment>
      {isAuthenticated && !redirectBack ? (
        <Redirect to={HOME_URL} />
      ) : (
        props.children
      )}
    </Fragment>
  );
};

const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(PublicRoute);
