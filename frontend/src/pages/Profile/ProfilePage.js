import React from "react";
import ChangeUserDetailsForm from "../../components/formElements/ChangeUserDetailsForm";
import { connect } from "react-redux";
import {
  addUserService,
  changeUserDetails,
  deleteUserService,
  getUnregisteredServiceClients,
  getUserProfile
} from "../../actions/profile";
import Modal from "../../components/Gallery/Modal";
import DetailBox from "../../components/DetailBox";
import { renderErrorMessage } from "../../components/Message";
import { displayBytesInReadableForm } from "../../functions";
import ManageServices from "./ManageServices";
import SideBarNav from "../../components/site layout/SideBarNav";
import { NavLink, Route, Switch } from "react-router-dom";
import PrivateRoute from "../../components/PrivateRoute";
import APIPage from "./APIPage";

class ProfilePage extends React.Component {
  state = { showAccountModal: false };

  componentDidMount() {
    if (!this.props.profile.user) {
      this.props.getUserProfile("/user/profile");
    }
    this.props.getUnregisteredServiceClients("/services/public/unregistered");
  }

  toggleModal = () => {
    this.setState({
      showAccountModal: !this.state.showAccountModal
    });
  };

  renderAccount() {
    const { user, storageQuota } = this.props.profile;

    return (
      <div className="ui padded grid">
        {this.props.profile.errorMessage &&
          renderErrorMessage(this.props.profile.errorMessage)}
        {user && (
          <div className="ui six wide column">
            <DetailBox header="Account Details">
              <p>
                <strong>Name:</strong> {user.username}
              </p>
              <p>
                <strong>Email:</strong> {user.email}
              </p>
              <p>
                <strong>Account Enabled:</strong> {user.enabled ? "Yes" : "No"}
              </p>
              <p>
                <strong>Role:</strong> {user.role.replace("ROLE_", "")}
              </p>
              <button
                className="ui primary button centerButton"
                onClick={this.toggleModal}
              >
                Edit
              </button>
            </DetailBox>
          </div>
        )}

        {storageQuota && (
          <div className="ui six wide column">
            <DetailBox header="Storage Quota Details">
              <p>
                <strong>Used Storage:</strong>{" "}
                {displayBytesInReadableForm(storageQuota.used)}
              </p>
              <p>
                <strong>Max Storage:</strong> {storageQuota.max} GB
              </p>
              <p>
                <strong>Ignore Quota:</strong>{" "}
                {storageQuota.ignoreQuota ? "Yes" : "No"}
              </p>
            </DetailBox>
          </div>
        )}

        {this.state.showAccountModal && (
          <Modal title="Edit Account" closeModal={this.toggleModal}>
            <div className="ui centered grid">
              <div className="six wide column">
                <ChangeUserDetailsForm
                  onSubmitSuccess={this.toggleModal}
                  initialValues={{ email: user.email, role: user.role }}
                />
              </div>
            </div>
          </Modal>
        )}
      </div>
    );
  }

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
              <Route exact path={match.path}>
                {this.renderAccount()}
              </Route>
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
const mapStateToProps = state => {
  return { profile: state.profile };
};

export default connect(mapStateToProps, {
  changeUserDetails,
  getUserProfile,
  getUnregisteredServiceClients,
  deleteUserService,
  addUserService
})(ProfilePage);
