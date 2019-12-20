import React from "react";
import { connect } from "react-redux";
import _ from "lodash";

import { deleteMedia, fetchAlbums, fetchUserImages } from "../../actions";
import Media from "../../components/Gallery/Media";
import "../../assets/css/layout.css";
import Modal from "../../components/Gallery/Modal";
import EditMediaForm from "../../components/formElements/EditMediaForm";
import RenderMedias from "../../components/Gallery/RenderMedias";
import { renderErrorMessage } from "../../components/Message";
import { BASE_URL } from "../../apis/api";

class UserGalleryPage extends React.Component {
  state = { isModalOpen: false };

  handleShowDialog = media => {
    this.setState({ isOpen: !this.state.isOpen, media });
  };

  componentDidMount() {
    this.props.fetchAlbum(`/gallery/myalbums/${this.props.user}`);
    this.props.fetchUserImages(`/gallery/medias/user/${this.props.user}`);
  }

  returnAlbumName = media => {
    let album = _.find(this.props.medias.albums, mediaAlbum => {
      return mediaAlbum.album.id === media.albumID;
    });
    return album ? album.album.name : "none";
  };

  render() {
    return (
      <div className="marginPadding">
        <h1 className="ui center aligned header">
          {this.props.user}'s Gallery
        </h1>
        <div className="ui container">
          <div className="ui four column centered grid">
            {RenderMedias(
              this.props.medias.userMediasList,
              this.props.medias.message,
              this.handleShowDialog
            )}
          </div>
        </div>
        {this.state.isOpen && (
          <Modal
            title={this.state.media.name}
            closeModal={() => this.handleShowDialog()}
          >
            <div className="image">
              <a
                href={`${BASE_URL}/api/gallery/${this.state.media.mediaType}/${this.state.media.fileName}`}
              >
                <Media
                  media={this.state.media}
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
                  `/gallery/media/delete/${this.state.media.id}`,
                  this.state.media.id
                );
                this.setState({ isOpen: false });
              }}
            >
              Delete
            </button>
            <EditMediaForm
              media={this.state.media}
              onSubmitSuccess={this.handleShowDialog}
              initialValues={{
                name: this.state.media.name,
                linkStatus: this.state.media.linkStatus,
                album: this.returnAlbumName(this.state.media)
              }}
            />
            {this.props.medias.deleteError &&
              renderErrorMessage(this.props.medias.deleteError)}
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
  fetchUserImages,
  deleteMedia,
  fetchAlbum: fetchAlbums
})(UserGalleryPage);
