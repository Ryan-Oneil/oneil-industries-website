import React from 'react';
import LazyLoad from 'react-lazyload';

class Media extends React.Component {

    renderImage = (media) => {
        const baseSrc = 'http://localhost:8080/api/gallery/image';
        const endpoint = `${this.props.fullSize ? "" : "/thumbnail"}/${media.fileName}`;

        return (
            <div className="image">
                <LazyLoad>
                    <img alt={media.name} className="ui centered image" src={baseSrc + endpoint}/>
                </LazyLoad>
            </div>
        )
    };

    renderVideo = (media, renderVideoControls) => {
        const src = `http://localhost:8080/api/gallery/video/${media.fileName}`;

        return <video className="centerVideo" src={src}  controls={renderVideoControls}/>;
    };

    renderMedia = ()=> {
        const {media, renderVideoControls} = this.props;

        if (media.mediaType === "video") {
            return this.renderVideo(media, renderVideoControls)
        }
        return this.renderImage(media);
    };

    render() {
        const {onClick, media} = this.props;

        return <div className="column imageBox" key={media.id} onClick={onClick}>
            {this.renderMedia()}
        </div>
    }
}
export default Media;