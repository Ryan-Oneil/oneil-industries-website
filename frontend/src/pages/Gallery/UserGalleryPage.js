import React from "react";
import { connect } from "react-redux";
import _ from "lodash";

import {
  deleteMedia,
  fetchAlbums,
  fetchImages,
  setActiveMediaForModal
} from "../../actions";
import Media from "../../components/Gallery/Media";
import "../../assets/css/layout.css";
import Modal from "../../components/Gallery/Modal";
import EditMediaForm from "../../components/formElements/EditMediaForm";
import RenderMedias from "../../components/Gallery/RenderMedias";
import { renderErrorMessage } from "../../components/Message";
import { BASE_URL } from "../../apis/api";

class UserGalleryPage extends React.Component {
  state = { isModalOpen: false };

  handleShowDialog = mediaID => {
    this.setState({
      isOpen: !this.state.isOpen
    });
    this.props.setActiveMediaForModal(mediaID);
  };

  componentDidMount() {
    this.props.fetchAlbums(`/gallery/myalbums/${this.props.user}`);
    this.props.fetchImages(`/gallery/medias/user/${this.props.user}`);
  }

  returnAlbumName = media => {
    let album = _.find(this.props.medias.albums, mediaAlbum => {
      return mediaAlbum.album.id === media.albumID;
    });
    return album ? album.album.name : "none";
  };

  render() {
    const { activeMedia, mediasList, deleteError } = this.props.medias;

    return (
      <div className="marginPadding">
        <h1 className="ui center aligned header">
          {this.props.user}'s Gallery
        </h1>
        <div className="ui container">
          <div className="ui four column centered grid">
            {RenderMedias(mediasList, this.handleShowDialog)}
          </div>
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
            <button
              value="Delete"
              className="centerButton ui negative button center aligned bottomMargin"
              onClick={() => {
                this.props.deleteMedia(
                  `/gallery/media/delete/${activeMedia.id}`,
                  activeMedia.id
                );
                this.setState({ isOpen: false });
              }}
            >
              Delete
            </button>
            <EditMediaForm
              media={activeMedia}
              initialValues={{
                name: activeMedia.name,
                privacy: activeMedia.linkStatus,
                album: this.returnAlbumName(activeMedia)
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
  deleteMedia,
  fetchAlbums,
  setActiveMediaForModal
})(UserGalleryPage);
