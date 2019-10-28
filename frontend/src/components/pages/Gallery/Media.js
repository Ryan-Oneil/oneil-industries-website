import React from 'react';

class Media extends React.Component {

    renderImage = (media) => {
        const src = `http://localhost:8080/api/gallery/media/thumbnail/${media.id}`;

        return (
            <div className="image">
                <img alt={media.name} className="ui centered image" src={src}/>
            </div>
        )
    };

    renderVideo = (media, renderVideoControls) => {
        const src = `http://localhost:8080/api/gallery/media/${media.id}`;

        return (
            <div className="ui">
                <video className="centerVideo" src={src}  controls={renderVideoControls} height="400" width="400"/>
            </div>
        )
    };

    renderMedia = (media, renderVideoControls)=> {
        if (media.mediaType === "video") {
            return this.renderVideo(media, renderVideoControls)
        }
        return this.renderImage(media);
    };

    render() {
        const {media, renderVideoControls, onClick} = this.props;

        return <div className="column imageBox" key={media.id} onClick={onClick}>
            {this.renderMedia(media, renderVideoControls)}
        </div>
    }
}
export default Media;