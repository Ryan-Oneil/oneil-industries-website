import React from "react";
import { connect } from "react-redux";
import { clearError, disableError } from "../actions/errors";

class ErrorModal extends React.Component {
  constructor(props) {
    super(props);

    this.setWrapperRef = this.setWrapperRef.bind(this);
    this.handleClickOutside = this.handleClickOutside.bind(this);
  }

  componentDidMount() {
    document.addEventListener("mousedown", this.handleClickOutside);
  }

  componentWillUnmount() {
    document.removeEventListener("mousedown", this.handleClickOutside);
  }

  setWrapperRef(node) {
    this.wrapperRef = node;
  }

  handleClickOutside(event) {
    const { clearError } = this.props;

    if (this.wrapperRef && !this.wrapperRef.contains(event.target)) {
      clearError();
    }
  }

  render() {
    const { error, clearError, disableError } = this.props;

    return (
      <div className="modalBackground">
        <div
          className="errorModal ui mini test modal transition visible active"
          ref={this.setWrapperRef}
        >
          <div className="header">An error occurred</div>
          <div className="content">
            <p>{error}</p>
          </div>
          <div className="actions">
            <div
              className="ui negative left floated button"
              onClick={disableError}
              data-tooltip="Temporarily disables error popups"
              data-position="bottom center"
            >
              Disable
            </div>
            <div className="ui positive button" onClick={clearError}>
              Close
            </div>
          </div>
        </div>
      </div>
    );
  }
}
export default connect(null, { clearError, disableError })(ErrorModal);
