import React from 'react';
import {Field, reduxForm} from "redux-form";
import {renderErrorMessage} from "../Message";
import {connect} from "react-redux";
import {updateAlbum} from "../../actions";

class AddServiceClientForm extends React.Component {

    state = {serviceList: []};

    componentDidMount() {
        this.setState({serviceList: this.props.serviceList})
    }

    onSubmit = (formValues) => {
        const newServiceClient = this.state.serviceList[formValues.position];

        this.setState({serviceList: this.state.serviceList.filter(serviceClient => serviceClient !== newServiceClient)});

        return this.props.addService("/user/profile/addservice/", this.props.service, newServiceClient);
    };

    renderServiceClient = () => {
        return this.state.serviceList.map((serviceClient, index) => {
            return (
                <option value={index} key={index}>{serviceClient.name}</option>
            );
        })
    };

    render() {
        const {service, submitting, errorMessage} = this.props;

        return (
            <form onSubmit={this.props.handleSubmit(this.onSubmit)} className="ui form error">
                <div className="ui segment">
                    <h3 className="ui centered aligned header">{service} client</h3>
                    <Field name="position" component="select" className="field">
                        <option value=""/>
                        {this.renderServiceClient()}
                    </Field>
                    {errorMessage && renderErrorMessage(errorMessage)}
                    <button className="ui fluid large navColor submit button" disabled={submitting}>Confirm</button>
                </div>
            </form>
        )
    }
}

export default connect(
    null,
    { updateAlbum }
)(reduxForm({ form: "addServiceClient" })(AddServiceClientForm));
