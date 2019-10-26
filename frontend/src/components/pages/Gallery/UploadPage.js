import React from 'react';
import { Field, reduxForm } from 'redux-form';

class UploadPage extends React.Component {

    renderError({error, touched}) {
        if (touched && error) {
            return (
                <div className="ui error message">
                    {<div className="header">{error}</div>}
                </div>
            );
        }
    }

    renderInput = ( {input, label, meta, iconType, type}) => {
        const className = `field ${meta.error && meta.touched ? `error` : ``}`;
        const iconClassName = `${iconType} icon`;

        return (
            <div className={className}>
                <div className="ui left icon input">
                    <i className={iconClassName}/>
                    <input {...input} autoComplete="off" type={type} placeholder={label}/>

                </div>
                {this.renderError(meta)}
            </div>
        );
    };

    onSubmit = (formValues) => {
        const dispatch = this.props.dispatch;
        const creds = { username: formValues.username.trim(), password: formValues.password.trim()};

        dispatch(loginUser(creds));
    };

    render() {

        const { errorMessage } = this.props;

        return (
            <div className="ui one column stackable center aligned page grid">
                <div className="column twelve wide">
                    <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                        <div className="ui segment">
                            <h1 className="textColorScheme">
                                Upload your Media
                            </h1>
                            <Field
                                name="media"
                                component={this.renderInput}
                                label="Select Media file"
                                iconType= "file image"
                                type="file"
                            />
                            <Field
                                name="name"
                                component={this.renderInput}
                                label="Enter Media name"
                                iconType= "tag"
                                type="text"
                            />
                            <Field name="linkStatus" component="select">
                                <option value="unlisted">Unlisted</option>
                                <option value="public">Public</option>
                                <option value="private">Private</option>
                            </Field>
                            <Field name="album" component="select">
                                <option/>
                                <option value="unlisted">Unlisted</option>
                                <option value="public">Public</option>
                                <option value="private">Private</option>
                            </Field>
                            {errorMessage && <div className="ui error message">
                                {<div className="header">{errorMessage}</div>}
                            </div>}
                            <button className="ui fluid large navColor submit button">Upload</button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}

const validate = (formValues) => {
    const errors = {};

    if (!formValues.username) {
        errors.username = "You must enter a Username";
    }
    if (!formValues.password) {
        errors.password = "You must enter a Password";
    }
    return errors;
};

export default reduxForm({
    form: 'upload',
    validate
})(UploadPage)
