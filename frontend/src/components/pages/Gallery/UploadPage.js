import React from 'react';
import { Field, reduxForm } from 'redux-form';
import {fetchAlbum, uploadMedia} from "../../../actions";
import {connect} from "react-redux";
import "../../../assets/css/layout.css"

class UploadPage extends React.Component {

    constructor(props) {
        super(props);

        this.props.medias.albums = props.dispatch(fetchAlbum(`/gallery/myalbums/${props.user}/names`));
    }

    renderError({error, touched}) {
        if (touched && error) {
            return (
                <div className="ui error message">
                    {<div className="header">{error}</div>}
                </div>
            );
        }
    }

    renderInput = ( {input, label, meta, type}) => {
        const className = `field ${meta.error && meta.touched ? `error` : ``}`;

        return (
            <div className={className}>
                <input {...input} autoComplete="off" type={type} placeholder={label}/>
                {this.renderError(meta)}
            </div>
        );
    };

    //Need a custom render field for file with redux due to errors
    renderFileField = ({ input, type, meta}) => {
        delete input.value;

        const className = `field ${meta.error && meta.touched ? `error` : ``}`;

        return (
            <div className={className}>
                <input {...input} type={type}/>
                {this.renderError(meta)}
            </div>
        )
    };

    renderAlbums = () => {
        if (this.props.medias.albums) {
            return this.props.medias.albums.map(album => {
                return (
                    <option value={album} key={album}>{album}</option>
                );
            })
        }
    };

    onSubmit = (formValues) => {
        const dispatch = this.props.dispatch;

        if (!this.props.medias.isPosting) {
            dispatch(uploadMedia("/gallery/upload", formValues));
        }
    };

    render() {
        const { errorMessage } = this.props;

        return (
            <div className="ui one column stackable center aligned page grid">
                <div className="column twelve wide">
                    <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                        <div className="ui segment">
                            <h1 className="textFormat">
                                Upload your Media
                            </h1>
                            <label className="textFormat">Choose File from your Computer</label>
                            <Field name="file" type="file" component={this.renderFileField} />
                            <label className="textFormat">Media Name</label>
                            <Field
                                name="name"
                                component={this.renderInput}
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
                            <button className="ui fluid large navColor submit button">Upload</button>
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
    mapStateToProps
)(UploadPage);

export default reduxForm({
    form: 'upload',
    validate,
    initialValues: {
        'linkStatus': 'unlisted',
        'album' : 'none'
    }
})(UploadPage)
