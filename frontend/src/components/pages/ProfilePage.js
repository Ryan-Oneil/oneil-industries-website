import React from 'react';
import ChangeUserDetailsForm from "../formElements/ChangeUserDetailsForm";
import {connect} from "react-redux";
import {renderInput} from "../formElements";
import {Field} from "redux-form";
import {
    addUserService,
    changeUserDetails,
    deleteUserService,
    getUnregisteredServiceClients,
    getUserProfile
} from "../../actions/profile";
import Modal from "./Gallery/Elements/Modal";
import DetailBox from "../DetailBox";
import UserServiceTable from "../Services/UserServiceTable";
import {renderErrorMessage} from "../Message";
import AddServiceClientForm from "../formElements/AddServiceClientForm";

class ProfilePage extends React.Component {

    state = { activeMenu : "account", showAccountModal: false, showAddServerModal: false, serviceList: null, service: "" };

    componentDidMount() {
        if (!this.props.profile.user) {
            this.props.getUserProfile("/user/profile");
        }
        this.props.getUnregisteredServiceClients("/services/public/unregistered");
    }

    renderAccount()  {
        const {user} = this.props.profile;

        return (
            <div>
                <div className="ui centered grid">
                    <div className="six wide column">
                        {user &&
                        <DetailBox header="Account Details">
                            <p><strong>Name:</strong> {user.username}</p>
                            <p><strong>Email:</strong> {user.email}</p>
                            <p><strong>Account Enabled:</strong> {user.enabled === 1 ? 'Yes' : 'No'}</p>
                            <p><strong>Role:</strong> {user.authorities[0].authority}</p>
                            <button className="ui primary button centerButton" onClick={() => this.setState({showAccountModal: !this.state.showAccountModal})}>Edit</button>
                        </DetailBox>}
                    </div>
                </div>
                {this.state.showAccountModal && (
                    <Modal
                        title="Edit Account"
                        closeModal = {() => this.setState({showAccountModal: !this.state.showAccountModal})}
                    >
                        <div className="ui centered grid">
                            <div className="six wide column">
                                <ChangeUserDetailsForm onSubmit={(formValues) => {return this.props.changeUserDetails(formValues, "/user/profile/update")}}
                                                       errorMessage={this.props.profile.errorMessage}
                                                       message = {this.props.profile.message}
                                >
                                    <Field name="email"
                                           component={renderInput}
                                           type="email"
                                           label="Email"
                                    />
                                </ChangeUserDetailsForm>
                            </div>
                        </div>
                    </Modal>)}
            </div>
        );
    }

    displayServiceModal = (serviceList, service) => {
        this.setState({showAddServerModal: true, serviceList, service})
    };

    renderServices() {
        const {userTeamspeak, userDiscord, teamspeakUsers, discordUsers} = this.props.profile;

        return (
            <div className="ui centered grid marginPadding">
                {userTeamspeak && <div className="ui six wide">
                    <div className="ui segment">
                        {UserServiceTable("Teamspeak", userTeamspeak, this.props.deleteUserService)}
                        <button className="ui primary button centerButton" onClick={() => {this.displayServiceModal(teamspeakUsers, "teamspeak")}}>Add</button>
                    </div>
                </div>
                }
                {userDiscord && <div className="ui six wide">
                    <div className="ui segment">
                        {UserServiceTable("Discord", userDiscord, this.props.deleteUserService)}
                        <button className="ui primary button centerButton" onClick={() => {this.displayServiceModal(discordUsers, "discord")}}>Add</button>
                    </div>
                </div>}
                {this.state.showAddServerModal && (
                    <Modal
                        title="Add Service"
                        closeModal = {() => this.setState({showAddServerModal: !this.state.showAddServerModal})}>
                        <div className="ui centered grid">
                            <div className="six wide column">
                                <AddServiceClientForm  addService={this.props.addUserService}
                                                       errorMessage={this.props.profile.errorMessage}
                                                       message = {this.props.profile.message}
                                                       service = {this.state.service}
                                                       serviceList = {this.state.serviceList}
                                />
                            </div>
                        </div>
                    </Modal>)}
            </div>
        );
    }

    renderMenu() {
        switch (this.state.activeMenu) {
            case "account": {
                return this.renderAccount()
            }
            case "services": {
                return this.renderServices()
            }
            default: {
                return this.renderAccount()
            }
        }
    }

    render() {
        return (
            <div>
                <div className="ui userNav massive menu">
                    <div className="ui buttons">
                        <button className="ui large button" onClick={() => {this.setState({activeMenu: "account"})}}>Account</button>
                        <button className="ui large button" onClick={() => {this.setState({activeMenu: "services"})}}>Services</button>
                    </div>
                </div>
                <div className="ui container">
                    {this.renderMenu()}
                </div>
                {this.props.profile.errorMessage && renderErrorMessage(this.props.profile.errorMessage)}
            </div>
        )
    }
}
const mapStateToProps = (state) => {
    return {profile: state.profile};
};

export default connect(
    mapStateToProps,
    { changeUserDetails,
        getUserProfile,
        getUnregisteredServiceClients,
        deleteUserService,
        addUserService}
)(ProfilePage);