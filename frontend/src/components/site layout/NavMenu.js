import React from "react";
import { NavLink } from "react-router-dom";
import "../../assets/css/layout.css";
import { logoutUser } from "../../actions";
import { connect } from "react-redux";

class NavMenu extends React.Component {
  render() {
    const { isAuthenticated, role } = this.props.auth;
    const isAdmin = role === "ROLE_ADMIN";

    return (
      <div className="ui navColor inverted massive secondary stackable fixed menu removeCircleBorder">
        {/*<NavLink to="/" className="centerItem removeFormat">*/}
        <h3 className="ui header logoText centerItem">
          <img
            src={require("../../assets/images/fact.png")}
            className="ui circular image"
            alt="Factory"
          />
          Oneil Industries
        </h3>
        {/*</NavLink>*/}
        <div className="right large menu redText" id="redText">
          <NavLink to="/" className="header item" exact={true}>
            Home
          </NavLink>
          <NavLink to="/contact" className="header item">
            Contact
          </NavLink>
          <NavLink to="/images" className="header item">
            Images
          </NavLink>
          <NavLink to="/services" className="header item">
            Services
          </NavLink>
          {isAuthenticated && (
            <NavLink to="/dashboard" className="header item">
              Dashboard
            </NavLink>
          )}
          {isAuthenticated && isAdmin && (
            <NavLink to="/admin" className="header item">
              Admin
            </NavLink>
          )}
        </div>

        <div className="secondary menu" id="secondMenu">
          {!isAuthenticated && (
            <NavLink to="/login" className="header item" id="headerButton">
              Login
            </NavLink>
          )}
          {isAuthenticated && (
            <div className="item">
              <button
                className="ui button"
                onClick={() => this.props.logoutUser()}
                id="headerButton"
              >
                Logout
              </button>
            </div>
          )}
        </div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { auth: state.auth };
};

export default connect(mapStateToProps, { logoutUser })(NavMenu);
