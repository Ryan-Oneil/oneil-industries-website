import React from "react";
import '../../../assets/css/images.css';

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

    renderImage = (media) => {
        const src = `http://localhost:8080/api/gallery/media/thumbnail/${media.id}`;

        return (
            <img className="ui centered image" src={src} onClick={this.handleShowDialog}/>
        )
    };

    renderVideo = (media) => {
        const src = `http://localhost:8080/api/gallery/media/${media.id}`;

        return (
            <video className="centerVideo" src={src} onClick={this.handleShowDialog} height="400" width="400" controls/>
        )
    };

    renderMedia = (media)=> {
        if (media.mediaType === "video") {
            return this.renderVideo(media)
        }
        return this.renderImage(media);
    };

    render() {
        const {media, closeModal} = this.props;

        return (
            <div ref={this.setWrapperRef} className="imageModal ui modal visible active">
                <i className="closeModal close icon" onClick={closeModal}/>
                <h1 className="ui center aligned header">{media.name}</h1>
                <div className="image">
                    {this.renderMedia(media)}
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