import React from 'react';
import MediaModal from "./MediaModal";

class Media extends React.Component {
    state = { isOpen: false };

    handleShowDialog = () => {
        this.setState({ isOpen: !this.state.isOpen });
    };

    renderImage = (media) => {
        const src = `http://localhost:8080/api/gallery/media/thumbnail/${media.id}`;

        return (
            <div className="ui">
                <h2 className="ui center aligned header">{media.name}</h2>
                <img alt={media.name} src={src} onClick={this.handleShowDialog}/>
            </div>
        )
    };

    renderVideo = (media) => {
        const src = `http://localhost:8080/api/gallery/media/${media.id}`;

        return (
            <div className="ui">
                <h2 className="ui center aligned header">{media.name}</h2>
                <video src={src} onClick={this.handleShowDialog} height="400" width="400"/>
            </div>
        )
    };

    renderMedia = (media)=> {
        if (media.mediaType === "video") {
            return this.renderVideo(media)
        }
        return this.renderImage(media);
    };

    render() {
        const {media} = this.props;

        const src = `http://localhost:8080/api/gallery/media/thumbnail/${media.id}`;

        return <div className="column imageBox" key={media.id}>
            {this.renderMedia(media)}
            {this.state.isOpen && (<MediaModal
                media={media}
                src={src}
                closeModal = {() => this.handleShowDialog()}>
                {this.props.children}
            </MediaModal>)}
        </div>
    }
}

export default Media;