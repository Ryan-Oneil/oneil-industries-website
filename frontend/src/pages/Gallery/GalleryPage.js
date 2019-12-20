import React from "react";
import { fetchImages } from "../../actions";
import { connect } from "react-redux";
import "../../assets/css/images.css";
import Media from "../../components/Gallery/Media";
import Modal from "../../components/Gallery/Modal";
import RenderMedias from "../../components/Gallery/RenderMedias";
import { Route, Switch } from "react-router-dom";
import UploadPage from "./UploadPage";
import UserGalleryPage from "./UserGalleryPage";
import UserAlbumsPage from "./UserAlbumsPage";
import PrivateRoute from "../../components/PrivateRoute";
import { BASE_URL } from "../../apis/api";

class GalleryPage extends React.Component {
  state = { isModalOpen: false };

  handleShowDialog = media => {
    this.setState({ isOpen: !this.state.isOpen, media });
  };

  componentDidMount() {
    this.props.fetchImages("/gallery/medias");
  }

  render() {
    const { match } = this.props;
    const { user } = this.props.auth;

    return (
      <Switch>
        <Route exact path={match.path}>
          {/*<div className="marginPadding">*/}
          <div className="ui container">
            <div className="ui four column centered grid">
              {RenderMedias(
                this.props.medias.mediasList,
                this.props.medias.message,
                this.handleShowDialog,
                true
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
              <div className="centerText">
                <p>Uploader: {this.state.media.uploader}</p>
                <p>Uploaded: {this.state.media.dateAdded}</p>
              </div>
            </Modal>
          )}
          {/*</div>*/}
        </Route>
        <PrivateRoute>
          <Route exact path={`${match.path}/upload`} component={UploadPage} />
          <Route
            exact
            path={`${match.path}/mygallery`}
            render={props => <UserGalleryPage {...props} user={user} />}
          />
          <Route
            exact
            path={`${match.path}/myalbums`}
            render={props => <UserAlbumsPage {...props} user={user} />}
          />
        </PrivateRoute>
      </Switch>
    );
  }
}

const mapStateToProps = state => {
  return { medias: state.medias, auth: state.auth };
};

export default connect(mapStateToProps, { fetchImages })(GalleryPage);
