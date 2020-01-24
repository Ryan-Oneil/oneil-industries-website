import React from "react";
import ManageServices from "./ManageServices";
import SideBarNav from "../../components/site layout/SideBarNav";
import { NavLink, Route, Switch } from "react-router-dom";
import PrivateRoute from "../../components/PrivateRoute";
import APIPage from "./APIPage";
import ProfilePage from "./ProfilePage";
import SubNavMenu from "../../components/site layout/SubNavMenu";
import UserGalleryPage from "./UserGalleryPage";
import UserAlbumsPage from "./UserAlbumsPage";
import { connect } from "react-redux";
import Upload from "./Upload";

class ProfileRouting extends React.Component {
  render() {
    const { match } = this.props;
    const { user } = this.props.auth;

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
          <SubNavMenu header="Media Gallery" icon="images outline">
            <NavLink
              to={`${match.path}/gallery/upload`}
              exact
              className="item subMenuItem"
            >
              <i className="icon upload " />
              Upload
            </NavLink>
            <NavLink
              to={`${match.path}/gallery/medias`}
              exact
              className="item subMenuItem"
            >
              <i className="icon image outline" />
              Medias
            </NavLink>
            <NavLink
              to={`${match.path}/gallery/albums`}
              exact
              className="item subMenuItem"
            >
              <i className="icon images" />
              Albums
            </NavLink>
          </SubNavMenu>
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
              <Route
                exact
                path={`${match.path}/gallery/medias`}
                render={props => <UserGalleryPage {...props} user={user} />}
              />
              <Route
                exact
                path={`${match.path}/gallery/albums`}
                render={props => <UserAlbumsPage {...props} user={user} />}
              />
              <Route
                exact
                path={`${match.path}/gallery/upload`}
                component={Upload}
              />
            </PrivateRoute>
          </Switch>
        </div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(ProfileRouting);
