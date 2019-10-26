import React from "react";

class MediaModal extends React.Component {

    constructor(props) {
        super(props);

        this.setWrapperRef = this.setWrapperRef.bind(this);
        this.handleClickOutside = this.handleClickOutside.bind(this);
    }

    componentDidMount() {
        document.addEventListener('mousedown', this.handleClickOutside);
    }

    componentWillUnmount() {
        document.removeEventListener('mousedown', this.handleClickOutside);
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
        const {media, src, closeModal} = this.props;

        return (
            <div ref={this.setWrapperRef} className="imageModal ui modal visible active">
                <i className="closeModal close icon" onClick={closeModal}/>
                <h1 className="ui center aligned header">{media.name}</h1>
                <div className="image">
                    <img className="ui centered image" src={src}/>
                </div>
                <div className="content">
                    <div className="centerText">
                        <p>Uploader: {media.uploader}</p>
                        <p>Uploaded: {media.dateAdded}</p>
                    </div>
                </div>
            </div>
        )
    }
}

export default MediaModal;