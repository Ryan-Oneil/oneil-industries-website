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
import { displayBytesInReadableForm } from "../../functions";
import { Card, Col, Row } from "antd";

class Overview extends React.Component {
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
    const { user, storageQuota } = this.props.profile;

    return (
      <Row gutter={[32, 32]}>
        {storageQuota && (
          <Col xs={24} sm={24} md={6} lg={6} xl={6}>
            <Card title="Storage Quota Details">
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
            </Card>
          </Col>
        )}
      </Row>
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
})(Overview);
