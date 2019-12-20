import React from "react";
import { connect } from "react-redux";
import { getRoles, getUserDetail, getUsers } from "../../actions/admin";
import DetailBox from "../../components/DetailBox";
import Modal from "../../components/Gallery/Modal";

import { displayBytesInReadableForm } from "../../functions";
import EditUserQuotaForm from "../../components/formElements/EditUserQuotaForm";
import AdminUserDetailsForm from "../../components/formElements/AdminUserDetailsForm";

class ManageUser extends React.Component {
  constructor(props) {
    super(props);
    const user = this.props.match.params.user;
    this.props.getUserDetail(`/admin/user/${user}`);
  }

  state = { showModal: "" };

  closeModal = () => {
    this.setState({ showModal: "" });
  };

  renderAccountModal() {
    const { details } = this.props.admin.user;

    return (
      <Modal
        title="Edit Account"
        closeModal={() => this.setState({ showModal: "" })}
      >
        <div className="ui centered grid">
          <div className="six wide column">
            <AdminUserDetailsForm
              onSubmitSuccess={this.closeModal}
              initialValues={{
                email: details.email,
                role: details.role,
                enabled: details.enabled
              }}
            />
          </div>
        </div>
      </Modal>
    );
  }

  renderQuotaModal() {
    const { storageQuota, details } = this.props.admin.user;

    return (
      <Modal title="Edit Quota" closeModal={this.closeModal}>
        <div className="ui centered grid">
          <div className="six wide column">
            <EditUserQuotaForm
              onSubmitSuccess={this.closeModal}
              ignoreQuota={storageQuota.ignoreQuota}
              user={details.username}
              initialValues={{
                max: storageQuota.max,
                ignoreQuota: storageQuota.ignoreQuota
              }}
            />
          </div>
        </div>
      </Modal>
    );
  }

  renderModal() {
    switch (this.state.showModal) {
      case "": {
        return;
      }
      case "account": {
        return this.renderAccountModal();
      }
      case "quota": {
        return this.renderQuotaModal();
      }
      default: {
        return;
      }
    }
  }

  render() {
    const { details } = this.props.admin.user;
    const { storageQuota } = this.props.admin.user;

    return (
      <div className="ui padded grid">
        {details && (
          <div className="six wide column">
            <DetailBox header="Account Details">
              <p>
                <strong>Name:</strong> {details.username}
              </p>
              <p>
                <strong>Email:</strong> {details.email}
              </p>
              <p>
                <strong>Account Enabled:</strong>{" "}
                {details.enabled ? "Yes" : "No"}
              </p>
              <p>
                <strong>Role:</strong> {details.role}
              </p>
              <button
                className="ui primary button centerButton"
                onClick={() => this.setState({ showModal: "account" })}
              >
                Edit
              </button>
            </DetailBox>
          </div>
        )}

        {storageQuota && (
          <div className="six wide column">
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
              <button
                className="ui primary button centerButton"
                onClick={() => this.setState({ showModal: "quota" })}
              >
                Edit
              </button>
            </DetailBox>
          </div>
        )}
        {this.state.showModal && this.renderModal()}
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { admin: state.admin };
};

export default connect(mapStateToProps, { getRoles, getUsers, getUserDetail })(
  ManageUser
);
