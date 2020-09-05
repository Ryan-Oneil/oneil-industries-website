import React from "react";
import Media from "../../components/Gallery/Media";
import { connect } from "react-redux";
import { deleteAlbum, fetchAlbums } from "../../actions";
import Modal from "../../components/Gallery/Modal";
import EditForm from "../../components/formElements/editAlbumForm";

class UserAlbumsPage extends React.Component {
  state = { isOpen: false };

  handleShowDialog = album => {
    this.setState({ isOpen: !this.state.isOpen, album });
  };

  componentDidMount() {
    this.props.fetchAlbum(`/gallery/myalbums/${this.props.user}`);
  }

  renderList = () => {
    return this.props.medias.albums.map(album => {
      return (
        <div className="column imageBox" key={album.id}>
          <div className="ui card">
            <Media
              media={album.medias[0]}
              onClick={this.handleShowDialog.bind(this, album)}
            />
            <h1 className="ui centered header">{album.name}</h1>
          </div>
        </div>
      );
    });
  };

  render() {
    const { album } = this.state;

    return (
      <div className="marginPadding">
        <div className="ui container">
          <div className="ui four column grid">{this.renderList()}</div>
        </div>
        {this.state.isOpen && (
          <Modal title={album.name} closeModal={() => this.handleShowDialog()}>
            <div className="image">
              <a href={`/gallery/album/${album.id}`}>
                <Media media={album.medias[0]} renderVideoControls={true} />
              </a>
            </div>
            <button
              value="Delete"
              className="centerButton ui negative button center aligned bottomMargin"
              onClick={() => {
                this.props.deleteAlbum("/gallery/myalbums/delete/", album.id);
                this.setState({ isOpen: false });
              }}
            >
              Delete
            </button>
            <div className="centerText">
              <EditForm
                album={album}
                initialValues={{
                  newAlbumName: album.name,
                  showUnlistedImages: album.showUnlistedImages
                }}
              />
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
  fetchAlbum: fetchAlbums,
  deleteAlbum
})(UserAlbumsPage);
