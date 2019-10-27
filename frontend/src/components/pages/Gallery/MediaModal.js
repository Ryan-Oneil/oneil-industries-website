import React from "react";
import '../../../assets/css/images.css';

class MediaModal extends React.Component {

    constructor(props) {
        super(props);

        this.setWrapperRef = this.setWrapperRef.bind(this);
        this.handleClickOutside = this.handleClickOutside.bind(this);
        this.thumbnailSrc = "http://localhost:8080/api/gallery/media/thumbnail/";
        this.fullMediaSrc = "http://localhost:8080/api/gallery/media/"
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

        return (
            <a href={`${this.fullMediaSrc + media.id}`}>
                <img alt={media.name} className="ui centered image" src={`${this.thumbnailSrc + media.id}`} onClick={this.handleShowDialog}/>
            </a>
        )
    };

    renderVideo = (media) => {
        return (
            <video className="centerVideo" src={`${this.fullMediaSrc + media.id}`} onClick={this.handleShowDialog} height="400" width="400" controls/>
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
                    {this.props.children}
                </div>
            </div>
        )
    }
}
export default MediaModal;