import React from 'react';
import {Field, reduxForm} from "redux-form";
import {connect} from "react-redux";
import {updateAlbum} from "../../actions";

class EditAlbumForm extends React.Component {

    onSubmit = (formValues) => {
        this.props.updateAlbum(`/gallery/myalbums/update/${this.props.album.name}`, formValues);
    };

    render() {
        const { submitting, errorMessage } = this.props;
        console.log(this.props);
        return (
            <div className="ui one column stackable center aligned page grid">
                <div className="column twelve wide">
                    <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                        <div className="ui segment">
                            <h1 className="textFormat">
                                Edit Album
                            </h1>
                            <label className="textFormat">Album Name</label>
                            <Field
                                name="name"
                                component="input"
                                type="text"
                            />
                            <label className="textFormat">Show unlisted images</label>
                            <Field name="showUnlistedImages"
                                   component="select"
                                   className="field"
                            >
                                <option value="true">Yes</option>
                                <option value="false">No</option>
                            </Field>
                            {errorMessage && <div className="ui error message">
                                {<div className="header">{errorMessage}</div>}
                            </div>}
                            <button className="ui fluid large navColor submit button" disabled={submitting}>Confirm</button>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
}
const validate = (formValues) => {
    const errors = {};

    if (!formValues.name) {
        errors.name = "A Album needs a name!";
    }
    return errors;
};

export default connect(
    null,
    { updateAlbum }
)(reduxForm({ form: "editAlbum", enableReinitialize: true, validate })(EditAlbumForm));

