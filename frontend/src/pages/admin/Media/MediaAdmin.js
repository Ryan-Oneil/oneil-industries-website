import React from "react";
import { connect } from "react-redux";
import RenderMedias from "../../../components/Gallery/RenderMedias";
import Modal from "../../../components/Gallery/Modal";
import { BASE_URL } from "../../../apis/api";
import EditMediaForm from "../../../components/formElements/EditMediaForm";
import { renderErrorMessage } from "../../../components/Message";
import {
  deleteMedia,
  fetchImages,
  setActiveMediaForModal
} from "../../../actions";
import Media from "../../../components/Gallery/Media";

class MediaAdmin extends React.Component {
  state = { isModalOpen: false };

  componentDidMount() {
    this.props.fetchImages("/admin/medias");
  }

  handleShowDialog = mediaID => {
    this.setState({
      isModalOpen: !this.state.isModalOpen
    });
    this.props.setActiveMediaForModal(mediaID);
  };

  render() {
    const { activeMedia, mediasList, deleteError } = this.props.medias;

    return (
      <div className="ui padded equal width grid">
        <div className="ui four column centered grid">
          {RenderMedias(mediasList, this.handleShowDialog, true)}
        </div>
        {this.state.isModalOpen && (
          <Modal title={activeMedia.name} closeModal={this.handleShowDialog}>
            <div className="image">
              <a
                href={`${BASE_URL}/gallery/${activeMedia.mediaType}/${activeMedia.fileName}`}
              >
                <Media
                  media={activeMedia}
                  renderVideoControls={true}
                  fullSize={true}
                />
              </a>
            </div>
            <button
              value="Delete"
              className="centerButton ui negative button center aligned bottomMargin"
              onClick={() => {
                this.props.deleteMedia(
                  `/gallery/media/delete/${activeMedia.id}`,
                  activeMedia.id
                );
                this.setState({ isModalOpen: false });
              }}
            >
              Delete
            </button>
            <EditMediaForm
              media={activeMedia}
              initialValues={{
                name: activeMedia.name,
                privacy: activeMedia.linkStatus
              }}
            />
            {deleteError && renderErrorMessage(deleteError)}
          </Modal>
        )}
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { medias: state.medias };
};

export default connect(mapStateToProps, {
  fetchImages,
  setActiveMediaForModal,
  deleteMedia
})(MediaAdmin);
