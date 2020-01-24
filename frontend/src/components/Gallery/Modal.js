import React from "react";
import "../../assets/css/images.css";

class Modal extends React.Component {
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
    const { closeModal } = this.props;

    if (this.wrapperRef && !this.wrapperRef.contains(event.target)) {
      closeModal();
    }
  }

  render() {
    const { closeModal, title } = this.props;

    return (
      <div className="modalBackground">
        <div
          ref={this.setWrapperRef}
          className="imageModal ui modal visible active"
        >
          <i className="closeModal close icon" onClick={closeModal} />
          <h1 className="ui center aligned header">{title}</h1>
          <div className="scrolling content textColorScheme">
            {this.props.children}
          </div>
        </div>
      </div>
    );
  }
}
export default Modal;
