import React from "react";
import Media from "../../components/Gallery/Media";
import RenderMedias from "../../components/Gallery/RenderMedias";
import Modal from "../../components/Gallery/Modal";
import { BASE_URL } from "../../apis/api";
import { connect } from "react-redux";
import { fetchAlbumWithImages, setActiveMediaForModal } from "../../actions";

class AlbumPage extends React.Component {
  state = { isOpen: false, message: null };

  handleShowDialog = mediaID => {
    this.setState({ isOpen: !this.state.isOpen });
    this.props.setActiveMediaForModal(mediaID);
  };

  componentDidMount() {
    const {
      match: { params }
    } = this.props;

    this.props.fetchAlbumWithImages(`/gallery/album/${params.albumName}`);
  }

  render() {
    const { activeMedia, mediasList, album } = this.props.medias;

    return (
      <div className="marginPadding">
        <h1 className="ui center aligned header whiteText">
          Album :{" "}
          {album.name === album.id ? `Created by ${album.creator}` : album.name}
        </h1>
        <div className="ui container">
          <div className="ui four column centered grid">
            {RenderMedias(mediasList, this.handleShowDialog, true)}
          </div>
        </div>
        {this.state.isOpen && (
          <Modal title={activeMedia.name} closeModal={this.handleShowDialog}>
            <div className="image">
              <a
                href={`${BASE_URL}/gallery/${activeMedia.mediaType}/${activeMedia.fileName}`}
              >
                <Media media={activeMedia} renderVideoControls={true} />
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
  return { medias: state.medias };
};

export default connect(mapStateToProps, {
  setActiveMediaForModal,
  fetchAlbumWithImages
})(AlbumPage);
