import React from "react";
import ManageServices from "./ManageServices";
import SideBarNav from "../../components/site layout/SideBarNav";
import { NavLink, Route, Switch } from "react-router-dom";
import PrivateRoute from "../../components/PrivateRoute";
import APIPage from "./APIPage";
import ProfilePage from "./ProfilePage";

class ProfileRouting extends React.Component {
  render() {
    const { match } = this.props;

    return (
      <div className="ui padded equal width grid">
        <SideBarNav headerText="Profile Dashboard" headerIcon="hdd">
          <NavLink to={match.path} className="item" exact={true}>
            <i className="icon chart user" />
            Account
          </NavLink>
          <NavLink to={`${match.path}/services`} className="item">
            <i className="icon globe" />
            Services
          </NavLink>
          <NavLink to={`${match.path}/api`} className="item">
            <i className="icon server" />
            Api
          </NavLink>
        </SideBarNav>
        <div className="sixteen wide mobile thirteen wide tablet thirteen wide computer right floated column">
          <Switch>
            <PrivateRoute>
              <Route exact path={match.path} component={ProfilePage} />
              <Route
                exact
                path={`${match.path}/services`}
                component={ManageServices}
              />
              <Route exact path={`${match.path}/api`} component={APIPage} />
            </PrivateRoute>
          </Switch>
        </div>
      </div>
    );
  }
}
export default ProfileRouting;
