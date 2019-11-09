import React from 'react';
import { Field, reduxForm } from 'redux-form';
import '../../assets/css/layout.css';
import {renderInput, renderTextArea} from "../formElements";
import {connect} from "react-redux";
import {sendContactForm} from "../../actions";
import {renderErrorMessage, renderPositiveMessage} from "../Message";

class Contact extends React.Component {

    onSubmit = (formValues) => {
        return this.props.sendContactForm("/contact/send", formValues);
    };

    render() {

        const { submitting } = this.props;
        const { errorMessage, message, contactFormSent } = this.props.contact;

        return (
            <div className="ui container marginPadding">
                <div className="ui segment">
                    <h1 className="ui center aligned header">Contact Oneil Industries</h1>
                    <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                        <Field
                            name="name"
                            component={renderInput}
                            label="Enter Name"
                        />
                        <Field
                            name="email"
                            component={renderInput}
                            label="Enter Email"
                        />
                        <Field
                            name="subject"
                            component={renderInput}
                            label="Enter Subject"
                        />
                        <Field
                            name="message"
                            component={renderTextArea}
                            label="Enter Message"
                        />
                        {errorMessage && renderErrorMessage(errorMessage)}
                        {message && renderPositiveMessage(message)}
                        <button className="ui button primary centerButton" disabled={submitting || contactFormSent}>Submit</button>
                    </form>
                </div>
            </div>
        );
    }
}

const validate = (formValues) => {
    const errors = {};

    if (!formValues.name) {
        errors.name = "You must enter your name";
    }
    if (!formValues.email) {
        errors.email = "You must enter your email";
    }
    if (!formValues.subject) {
        errors.subject = "You must enter a subject";
    }
    if (!formValues.message) {
        errors.message = "You must enter your message";
    }
    return errors;
};

const mapStateToProps = (state) => {
    return {contact: state.contact};
};

export default connect(
    mapStateToProps,
    { sendContactForm }
)(reduxForm({ form: "contact", enableReinitialize: true, validate })(Contact));