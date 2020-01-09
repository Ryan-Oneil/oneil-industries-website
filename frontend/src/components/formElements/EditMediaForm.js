import React from "react";
import { Field, reduxForm } from "redux-form";
import { connect } from "react-redux";
import { clearMessages, updateMedia } from "../../actions";
import { renderInput } from "./index";
import { renderErrorMessage, renderPositiveMessage } from "../Message";

class EditMediaForm extends React.Component {
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
    return this.props.updateMedia(
      `/gallery/media/update/${this.props.media.id}`,
      formValues,
      this.props.media.id
    );
  };

  componentWillUnmount() {
    this.props.clearMessages();
  }

  render() {
    const { submitting, error, submitSucceeded, handleSubmit } = this.props;
    const { mediaUpdateMessage } = this.props.medias;

    return (
      <div className="ui one column stackable center aligned page grid">
        <div className="column twelve wide">
          <form
            onSubmit={handleSubmit(this.onSubmit)}
            className="ui form error"
          >
            <div className="ui segment">
              <h1 className="textFormat">Edit Media</h1>
              <label className="textFormat">Media Name</label>
              <Field name="name" component={renderInput} type="text" />
              <label className="textFormat">Link Status</label>
              <Field name="privacy" component="select" className="field">
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
              {mediaUpdateMessage && renderPositiveMessage(mediaUpdateMessage)}
              <button
                className="ui fluid large navColor submit button"
                disabled={submitting || submitSucceeded}
              >
                Confirm
              </button>
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
    errors.name = "A Media needs a name!";
  }
  return errors;
};

const mapStateToProps = state => ({
  medias: state.medias
});

export default connect(mapStateToProps, { updateMedia, clearMessages })(
  reduxForm({
    form: "editImage",
    enableReinitialize: true,
    validate
  })(EditMediaForm)
);
