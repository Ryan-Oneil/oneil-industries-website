import React from "react";
import { NavLink } from "react-router-dom";
import "../../assets/css/layout.css";
import { logoutUser } from "../../actions";
import DropDownNav from "./DropDownNav";
import { connect } from "react-redux";

class NavMenu extends React.Component {
  render() {
    const { isAuthenticated, role } = this.props.auth;
    const isAdmin = role === "ROLE_ADMIN";

    return (
      <div className="ui removeMargin navColor inverted huge menu removeCircleBorder">
        <div className="ui container">
          <div className="right menu">
            <NavLink to="/" className="header item" exact={true}>
              Home
            </NavLink>
            <NavLink to="/contact" className="header item">
              Contact
            </NavLink>
            <DropDownNav />
            <NavLink to="/services" className="header item">
              Services
            </NavLink>
            {isAuthenticated && (
              <NavLink to="/profile" className="header item">
                Profile
              </NavLink>
            )}
            {isAuthenticated && isAdmin && (
              <NavLink to="/admin" className="header item">
                Admin
              </NavLink>
            )}
          </div>

          <div className="right menu">
            {!isAuthenticated && (
              <NavLink to="/login" className="header item">
                Login
              </NavLink>
            )}
            {isAuthenticated && (
              <div className="item">
                <button
                  className="ui button navColor navTextColor"
                  onClick={() => this.props.logoutUser()}
                >
                  Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { auth: state.auth };
};

export default connect(mapStateToProps, { logoutUser })(NavMenu);
