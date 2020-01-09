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

  render() {
    const { user, storageQuota, errorMessage } = this.props.profile;

    return (
      <div className="ui padded grid">
        {errorMessage && renderErrorMessage(errorMessage)}
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
