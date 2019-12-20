import React from "react";
import { apiPostCall } from "../apis/api";
import {
  renderErrorMessage,
  renderPositiveMessage
} from "../components/Message";

class EmailConfirmationPage extends React.Component {
  state = { successMessage: "", errorMessage: "" };

  apiCall(token) {
    apiPostCall(this.props.endpoint + token)
      .then(result => {
        this.setState({ successMessage: result.data });
      })
      .catch(error => {
        if (error.response) {
          this.setState({ errorMessage: error.response.data });
        } else {
          this.setState({ errorMessage: error.message });
        }
      });
  }

  componentDidMount() {
    let token = this.props.match.params.token;

    if (!token) {
      this.props.history.push("/");
    }
    this.apiCall(token);
  }

  render() {
    const { errorMessage, successMessage } = this.state;

    return (
      <div className="ui centered grid marginPadding">
        {successMessage && renderPositiveMessage(successMessage)}
        {errorMessage && renderErrorMessage(errorMessage)}
      </div>
    );
  }
}
export default EmailConfirmationPage;
