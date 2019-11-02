import React from 'react';
import LazyLoad from 'react-lazyload';

class Media extends React.Component {

    renderImage = (media) => {
        const src = `http://localhost:8080/api/gallery/media/thumbnail/${media.id}`;

        return (
            <div className="image">
                <LazyLoad>
                    <img alt={media.name} className="ui centered image" src={src}/>
                </LazyLoad>
            </div>
        )
    };

    renderVideo = (media, renderVideoControls) => {
        const src = `http://localhost:8080/api/gallery/media/${media.id}`;

        return <video className="centerVideo" src={src}  controls={renderVideoControls}/>;
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