import React from "react";
import { connect } from "react-redux";
import {
  approvePublicMedia,
  denyPublicMedia,
  getMediaApprovals
} from "../../../actions/admin";
import ApprovalCard from "../../../components/Gallery/ApprovalCard";
import { BASE_URL } from "../../../apis/api";
import Modal from "../../../components/Gallery/Modal";
import Media from "../../../components/Gallery/Media";

class Approval extends React.Component {
  constructor(props) {
    super(props);
    this.props.getMediaApprovals("/admin/medias/pendingApproval");
  }
  state = { isModalOpen: false, media: {} };

  handleShowDialog = media => {
    this.setState({ isModalOpen: !this.state.isModalOpen, media });
  };

  renderApprovalList = () => {
    return this.props.admin.mediaApprovals.map(mediaApproval => {
      return (
        <div className="three wide column">
          <ApprovalCard
            media={mediaApproval.media}
            onClick={this.handleShowDialog.bind(this, mediaApproval.media)}
            uploaderName={mediaApproval.media.uploader}
            fileName={mediaApproval.publicName}
            dateAdded={mediaApproval.media.dateAdded}
            onApproveClick={() => {
              this.props.approvePublicMedia(
                `/admin/media/${mediaApproval.id}/approve`,
                mediaApproval.id
              );
            }}
            onDeclineClick={() => {
              this.props.denyPublicMedia(
                `/admin/media/${mediaApproval.id}/deny`,
                mediaApproval.id
              );
            }}
          />
        </div>
      );
    });
  };

  render() {
    const { media } = this.state;

    return (
      <div className="ui padded equal width grid">
        {this.renderApprovalList()}
        {this.state.isModalOpen && (
          <Modal title={media.name} closeModal={this.handleShowDialog}>
            <div className="image">
              <a
                href={`${BASE_URL}/api/gallery/${media.mediaType}/${media.fileName}`}
              >
                <Media
                  media={media}
                  renderVideoControls={true}
                  fullSize={true}
                />
              </a>
            </div>
            <div className="centerText">
              <p>Uploader: {media.uploader}</p>
              <p>Uploaded: {media.dateAdded}</p>
            </div>
          </Modal>
        )}
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { admin: state.admin, medias: state.medias };
};

export default connect(mapStateToProps, {
  getMediaApprovals,
  approvePublicMedia,
  denyPublicMedia
})(Approval);
