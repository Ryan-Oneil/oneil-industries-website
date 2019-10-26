import React from 'react';
import { Field, reduxForm } from 'redux-form';

class Contact extends React.Component {

    renderError({error, touched}) {
        if (touched && error) {
            return (
                <div className="ui error message">
                    <div className="header">{error}</div>
                </div>
            );
        }
    }

    renderInput = ( {input, label, meta}) => {
        const className = `field ${meta.error && meta.touched ? `error` : ``}`;

        return (
            <div className={className}>
                <label>{label}</label>
                <input {...input} autoComplete="off"/>
                {this.renderError(meta)}
            </div>
        );
    };

    renderTextArea = ( {input, label, meta}) => {
        const className = `field ${meta.error && meta.touched ? `error` : ``}`;

        return (
            <div className={className}>
                <label>{label}</label>
                <textarea {...input} autoComplete="off" rows="10" cols="40"/>
                {this.renderError(meta)}
            </div>
        );
    };

    onSubmit(formValues) {

    }

    render() {
        return (
            <div className="ui container">
                <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                    <Field
                        name="Name"
                        component={this.renderInput}
                        label="Enter Name"
                    />
                    <Field
                        name="Email"
                        component={this.renderInput}
                        label="Enter Email"
                    />
                    <Field
                        name="Subject"
                        component={this.renderInput}
                        label="Enter Subject"
                    />
                    <Field
                        name="Message"
                        component={this.renderTextArea}
                        label="Enter Message"
                    />
                    <button className="ui button primary">Submit</button>
                </form>
            </div>
        );
    }
}

const validate = (formValues) => {
    const errors = {};

    if (!formValues.title) {
        errors.title = "You must enter a title";
    }
    if (!formValues.description) {
        errors.description = "You must enter a description";
    }
    return errors;
};

export default reduxForm({
    form: 'streamCreate',
    validate
})(Contact);