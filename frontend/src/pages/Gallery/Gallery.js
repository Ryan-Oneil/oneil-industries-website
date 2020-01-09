import React from "react";
import RenderMedias from "../../components/Gallery/RenderMedias";
import Modal from "../../components/Gallery/Modal";
import { BASE_URL } from "../../apis/api";
import Media from "../../components/Gallery/Media";
import { connect } from "react-redux";
import { fetchImages, setActiveMediaForModal } from "../../actions";

class Gallery extends React.Component {
  state = { isModalOpen: false };

  handleShowDialog = mediaID => {
    this.setState({ isOpen: !this.state.isOpen });
    this.props.setActiveMediaForModal(mediaID);
  };

  componentDidMount() {
    this.props.fetchImages("/gallery/medias");
  }

  render() {
    const { activeMedia, mediasList } = this.props.medias;

    return (
      <div className="ui container">
        <div className="ui four column centered grid">
          {RenderMedias(mediasList, this.handleShowDialog, true)}
        </div>
        {this.state.isOpen && (
          <Modal title={activeMedia.name} closeModal={this.handleShowDialog}>
            <div className="image">
              <a
                href={`${BASE_URL}/api/gallery/${activeMedia.mediaType}/${activeMedia.fileName}`}
              >
                <Media
                  media={activeMedia}
                  renderVideoControls={true}
                  fullSize={true}
                />
              </a>
            </div>
            <div className="centerText">
              <p>Uploader: {activeMedia.uploader}</p>
              <p>Uploaded: {activeMedia.dateAdded}</p>
            </div>
          </Modal>
        )}
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { medias: state.medias, auth: state.auth };
};

export default connect(mapStateToProps, {
  fetchImages,
  setActiveMediaForModal
})(Gallery);
