import React from "react";
import UserServiceTable from "../../components/Services/UserServiceTable";
import Modal from "../../components/Gallery/Modal";
import AddServiceClientForm from "../../components/formElements/AddServiceClientForm";
import { connect } from "react-redux";
import {
  addUserService,
  deleteUserService,
  getUnregisteredServiceClients
} from "../../actions/profile";
import DetailBox from "../../components/DetailBox";

class ManageServices extends React.Component {
  state = { showAddServerModal: false, serviceList: null, service: "" };

  displayServiceModal = (serviceList, service) => {
    this.setState({ showAddServerModal: true, serviceList, service });
  };

  closeModal = () => {
    this.setState({
      showAddServerModal: !this.state.showAddServerModal
    });
  };

  render() {
    const {
      userTeamspeak,
      userDiscord,
      teamspeakUsers,
      discordUsers
    } = this.props.profile;

    return (
      <div className="ui padded grid">
        {userTeamspeak && (
          <div className="ui six wide column">
            <DetailBox header="Teamspeak">
              {UserServiceTable(
                userTeamspeak,
                this.props.deleteUserService,
                "teamspeak"
              )}
              <button
                className="ui primary button centerButton"
                onClick={() => {
                  this.displayServiceModal(teamspeakUsers, "teamspeak");
                }}
              >
                Add
              </button>
            </DetailBox>
          </div>
        )}
        {userDiscord && (
          <div className="ui six wide column">
            <DetailBox header="Discord">
              {UserServiceTable(
                userDiscord,
                this.props.deleteUserService,
                "discord"
              )}
              <button
                className="ui primary button centerButton"
                onClick={() => {
                  this.displayServiceModal(discordUsers, "discord");
                }}
              >
                Add
              </button>
            </DetailBox>
          </div>
        )}
        {this.state.showAddServerModal && (
          <Modal title="Add Service" closeModal={this.closeModal}>
            <div className="ui centered grid">
              <div className="six wide column">
                <AddServiceClientForm
                  addService={this.props.addUserService}
                  service={this.state.service}
                  serviceList={this.state.serviceList}
                  onSubmitSuccess={this.closeModal}
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
  getUnregisteredServiceClients,
  deleteUserService,
  addUserService
})(ManageServices);
