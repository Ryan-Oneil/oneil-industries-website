import React from "react";
import { connect } from "react-redux";
import "../../assets/css/images.css";
import { Route, Switch } from "react-router-dom";
import UploadPage from "./UploadPage";
import UserGalleryPage from "./UserGalleryPage";
import UserAlbumsPage from "./UserAlbumsPage";
import PrivateRoute from "../../components/PrivateRoute";
import Gallery from "./Gallery";

class GalleryRouting extends React.Component {
  render() {
    const { match } = this.props;
    const { user } = this.props.auth;

    return (
      <Switch>
        <Route exact path={match.path} component={Gallery} />
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
  return { auth: state.auth };
};

export default connect(mapStateToProps)(GalleryRouting);
