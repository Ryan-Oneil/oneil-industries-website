import React from 'react';
import { Field, reduxForm, reset } from 'redux-form';
import {fetchAlbums, uploadMedia} from "../../../actions";
import {connect} from "react-redux";
import "../../../assets/css/layout.css"
import {renderFileField, renderInput} from "../../formElements";

class UploadPage extends React.Component {

    constructor(props) {
        super(props);
        if (!this.props.medias.albums) {
            this.props.medias.albums = props.fetchAlbum(`/gallery/myalbums/${props.user}`);
        }
    }

    renderAlbums = () => {
        if (this.props.medias.albums) {
            return this.props.medias.albums.map(MediaAlbum => {
                return (
                    <option value={MediaAlbum.album.name} key={MediaAlbum.album.id}>{MediaAlbum.album.name}</option>
                );
            })
        }
    };

    onSubmit = (formValues) => {
        if (!this.props.medias.isPosting) {
            return this.props.uploadMedia("/gallery/upload", formValues);
            //this.props.reset('upload');
        }
    };

    render() {
        const { submitting, errorMessage } = this.props;

        return (
            <div className="ui one column stackable center aligned page grid">
                <div className="column twelve wide">
                    <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                        <div className="ui segment">
                            <h1 className="textFormat">
                                Upload your Media
                            </h1>
                            <label className="textFormat">Choose File from your Computer</label>
                            <Field name="file" type="file" component={renderFileField} />
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
                            {errorMessage && <div className="ui error message">
                                {<div className="header">{errorMessage}</div>}
                            </div>}
                            <button className="ui fluid large navColor submit button" disabled={submitting}>Upload</button>
                            {this.props.medias.mediaPostMessage && <div className="ui message">
                                {<div className="header">{this.props.medias.mediaPostMessage}</div>}
                            </div>}
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}

const validate = (formValues) => {
    const errors = {};

    if (!formValues.name) {
        errors.name = "You must enter a Name";
    }
    if (!formValues.file) {
        errors.file = "You must select a file";
    }
    return errors;
};

const mapStateToProps = (state) => ({
    medias: state.medias
});

UploadPage = connect(
    mapStateToProps,
    {uploadMedia, fetchAlbum: fetchAlbums, reset}
)(UploadPage);

export default reduxForm({
    form: 'upload',
    validate,
    initialValues: {
        'linkStatus': 'unlisted',
        'album' : 'none'
    }
})(UploadPage)
