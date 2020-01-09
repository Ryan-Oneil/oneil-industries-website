import React from "react";
import { Field, reduxForm, reset } from "redux-form";
import { fetchAlbums, uploadMedia } from "../../actions";
import { connect } from "react-redux";
import "../../assets/css/layout.css";
import { renderFileField, renderInput } from "../../components/formElements";
import {
  renderErrorMessage,
  renderPositiveMessage
} from "../../components/Message";
import { getUserStorage } from "../../actions/profile";

class UploadPage extends React.Component {
  constructor(props) {
    super(props);
    this.props.fetchAlbums(`/gallery/myalbums/${this.props.auth.user}`);
    this.props.getUserStorage("/user/profile/storageQuota");
  }
  state = { reachedUploadLimit: false };

  renderAlbums = () => {
    return this.props.medias.albums.map(MediaAlbum => {
      return (
        <option value={MediaAlbum.album.name} key={MediaAlbum.album.id}>
          {MediaAlbum.album.name}
        </option>
      );
    });
  };

  onSubmit = formValues => {
    return this.props.uploadMedia("/gallery/upload", formValues);
  };

  enforceFileSize = event => {
    const file = event.target.files[0];

    //Checks if a file was entered
    if (file) {
      const { storageQuota } = this.props.profile;
      const max = storageQuota.max * 1000000000;
      const used = storageQuota.used + event.target.files[0].size;

      if (!storageQuota.ignoreQuota && max - used < 0) {
        this.setState({ reachedUploadLimit: true });
      }
    }
  };

  render() {
    const { submitting, error } = this.props;
    const { mediaPostMessage } = this.props.medias;

    return (
      <div className="ui one column stackable center aligned page grid marginPadding">
        <div className="column twelve wide">
          <form
            onSubmit={this.props.handleSubmit(this.onSubmit)}
            className="ui form error"
          >
            <div className="ui segment">
              <h1 className="textFormat">Upload your Media</h1>
              <label className="textFormat">
                Choose File from your Computer
              </label>
              <Field
                name="file"
                type="file"
                component={renderFileField}
                onChange={this.enforceFileSize}
              />
              <label className="textFormat">Media Name</label>
              <Field
                name="name"
                component={renderInput}
                label="Enter Media name"
                type="text"
              />
              <label className="textFormat">Link Status</label>
              <Field name="linkStatus" component="select" className="field">
                <option value="unlisted">Unlisted</option>
                <option value="public">Public</option>
                <option value="private">Private</option>
              </Field>
              <label className="textFormat">Album</label>
              <Field name="album" component="select" className="field">
                <option value="none">None</option>
                {this.renderAlbums()}
              </Field>
              {error && renderErrorMessage(error)}
              {this.state.reachedUploadLimit &&
                renderErrorMessage("You have reached the upload storage limit")}
              <button
                className="ui fluid large navColor submit button"
                disabled={submitting}
              >
                Upload
              </button>
              {mediaPostMessage && renderPositiveMessage(mediaPostMessage)}
            </div>
          </form>
        </div>
      </div>
    );
  }
}

const validate = formValues => {
  const errors = {};

  if (!formValues.name) {
    errors.name = "You must enter a Name";
  }
  if (!formValues.file) {
    errors.file = "You must select a file";
  }
  return errors;
};

const mapStateToProps = state => ({
  medias: state.medias,
  profile: state.profile,
  auth: state.auth
});

UploadPage = connect(mapStateToProps, {
  uploadMedia,
  fetchAlbums,
  getUserStorage,
  reset
})(UploadPage);

export default reduxForm({
  form: "upload",
  validate,
  initialValues: {
    linkStatus: "unlisted",
    album: "none"
  }
})(UploadPage);
