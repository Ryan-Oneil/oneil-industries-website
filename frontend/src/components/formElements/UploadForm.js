import React from "react";
import { Field, formValueSelector, reduxForm, reset } from "redux-form";
import { renderErrorMessage, renderPositiveMessage } from "../Message";
import { connect } from "react-redux";
import { clearMessages, fetchAlbums, uploadMedia } from "../../actions";
import { getUserStorage } from "../../actions/profile";
import { renderInput, renderSelectField } from "./index";

class UploadForm extends React.Component {
  componentDidMount() {
    this.props.fetchAlbums(`/gallery/myalbums/${this.props.auth.user}`);
  }
  state = { reachedUploadLimit: false };
  renderAlbums = () => {
    return this.props.medias.albums.map(MediaAlbum => {
      return (
        <option value={MediaAlbum.id} key={MediaAlbum.id}>
          {MediaAlbum.name}
        </option>
      );
    });
  };

  componentWillUnmount() {
    this.props.clearMessages();
  }

  onSubmit = formValues => {
    return this.props.uploadMedia(
      "/gallery/upload",
      formValues,
      this.props.mediasList
    );
  };

  render() {
    const {
      submitting,
      error,
      handleSubmit,
      reachedUploadLimit,
      hasAlbumValue,
      mediasList
    } = this.props;
    const { mediaPostMessage } = this.props.medias;
    const showNewAlbum = hasAlbumValue === "new";

    return (
      <div className="column six wide">
        <form
          onSubmit={handleSubmit(this.onSubmit)}
          className="ui form error centerText"
        >
          <div className="ui segment">
            <h1 className="textFormat">Upload your Media(s)</h1>

            <label className="textFormat">Link Status</label>
            <Field
              name="linkStatus"
              component={renderSelectField}
              className="field"
            >
              <option value="unlisted">Unlisted</option>
              <option value="public">Public</option>
              <option value="private">Private</option>
            </Field>

            <label className="textFormat">Album</label>
            <Field name="album" component="select" className="field">
              <option value="" />
              <option value="new">Create new Album</option>
              {this.renderAlbums()}
            </Field>

            {showNewAlbum && (
              <>
                <label className="textFormat">Album Name</label>
                <Field name="albumName" component={renderInput} type="text" />
                <label className="textFormat">Show unlisted media</label>
                <Field
                  name="showUnlisted"
                  component={renderSelectField}
                  className="field"
                >
                  <option value="true">Yes</option>
                  <option value="false">No</option>
                </Field>
              </>
            )}

            {error && renderErrorMessage(error)}
            {reachedUploadLimit &&
              renderErrorMessage("You have reached the upload storage limit")}
            <button
              className="ui fluid large submit button buttonFormat"
              disabled={submitting || reachedUploadLimit || mediasList < 1}
            >
              Upload
            </button>
            {mediaPostMessage && renderPositiveMessage(mediaPostMessage)}
          </div>
        </form>
      </div>
    );
  }
}

const warn = values => {
  const warnings = {};
  if (values.linkStatus === "public") {
    warnings.linkStatus =
      "Public media may require admin approval. It will remain unlisted until approved";
  }
  return warnings;
};

const mapStateToProps = state => ({
  medias: state.medias,
  profile: state.profile,
  auth: state.auth,
  hasAlbumValue: selector(state, "album")
});
const selector = formValueSelector("upload");
UploadForm = connect(mapStateToProps, {
  uploadMedia,
  fetchAlbums,
  getUserStorage,
  clearMessages,
  reset
})(UploadForm);

export default reduxForm({
  form: "upload",
  warn,
  initialValues: {
    linkStatus: "unlisted",
    showUnlisted: "true"
  }
})(UploadForm);
