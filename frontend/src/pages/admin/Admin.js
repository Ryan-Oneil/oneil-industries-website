import React from "react";
import { connect } from "react-redux";
import { getUsers } from "../../actions/admin";
import { NavLink, Route, Switch } from "react-router-dom";
import Users from "./Users";
import ManageUser from "./ManageUser";
import PrivateRoute from "../../components/PrivateRoute";
import SideBarNav from "../../components/site layout/SideBarNav";

class Admin extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { match } = this.props;
    return (
      <div className="ui padded equal width grid">
        <SideBarNav headerText="Admin Dashboard" headerIcon="hdd">
          <NavLink to={match.path} className="item" exact={true}>
            <i className="icon chart bar" />
            Stats
          </NavLink>
          <NavLink to={`${match.path}/users`} className="item">
            <i className="icon user" />
            Users
          </NavLink>
          <NavLink to={`${match.path}/settings`} className="item">
            <i className="icon cog" />
            Settings
          </NavLink>
        </SideBarNav>

        <div className="sixteen wide mobile thirteen wide tablet thirteen wide computer right floated column">
          <Switch>
            <PrivateRoute>
              <Route exact path={match.path}>
                <div className="two wide column">
                  <h3>Under construction</h3>
                </div>
              </Route>
              <Route exact path={`${match.path}/users`} component={Users} />
              <Route
                path={`${match.path}/users/:user`}
                component={ManageUser}
              />
            </PrivateRoute>
          </Switch>
        </div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { admin: state.admin };
};

export default connect(mapStateToProps, { getUsers })(Admin);
