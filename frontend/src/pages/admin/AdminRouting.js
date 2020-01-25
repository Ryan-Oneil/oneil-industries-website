import React from "react";
import { connect } from "react-redux";
import { getMediaApprovals } from "../../actions/admin";
import { NavLink, Route, Switch } from "react-router-dom";
import Users from "./Users";
import ManageUser from "./ManageUser";
import PrivateRoute from "../../components/PrivateRoute";
import SideBarNav from "../../components/site layout/SideBarNav";
import Media from "./Media/MediaAdmin";
import SubNavMenu from "../../components/site layout/SubNavMenu";
import Approval from "./Media/Approval";
import Stats from "./Stats";

class AdminRouting extends React.Component {
  componentDidMount() {
    const { isAuthenticated, role } = this.props.auth;

    if (!isAuthenticated || role !== "ROLE_ADMIN") {
      this.props.history.push("/");
    }
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
          <SubNavMenu header="Media Gallery" icon="images outline">
            <NavLink
              to={`${match.path}/media`}
              exact
              className="item subMenuItem"
            >
              <i className="icon images outline" />
              Medias
            </NavLink>
            <NavLink
              to={`${match.path}/media/approval`}
              exact
              className="item subMenuItem"
            >
              <i className="icon check" />
              Approvals
            </NavLink>
          </SubNavMenu>
        </SideBarNav>

        <div className="sixteen wide mobile thirteen wide tablet thirteen wide computer right floated column">
          <Switch>
            <PrivateRoute>
              <Route exact path={match.path} component={Stats} />
              <Route exact path={`${match.path}/users`} component={Users} />
              <Route
                path={`${match.path}/users/:user`}
                component={ManageUser}
              />
              <Route exact path={`${match.path}/media`} component={Media} />
              <Route
                exact
                path={`${match.path}/media/approval`}
                component={Approval}
              />
            </PrivateRoute>
          </Switch>
        </div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { admin: state.admin, auth: state.auth };
};

export default connect(mapStateToProps, { getMediaApprovals })(AdminRouting);
