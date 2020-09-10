import React from "react";
import { BASE_URL } from "../../apis/api";
import { Image } from "antd";

class Media extends React.Component {
  renderImage = media => {
    const baseSrc = `${BASE_URL}/gallery/image`;
    const endpoint = `${this.props.fullSize ? "" : "/thumbnail"}/${
      media.fileName
    }`;
    //Checks if media.src exists as some pages send blobs instead or urls
    return (
      <Image
        alt={media.name}
        src={media.src ? media.src : baseSrc + endpoint}
        preview={false}
        style={{ margin: "auto" }}
        width={"100%"}
      />
    );
  };

  renderVideo = (media, renderVideoControls) => {
    const src = `${BASE_URL}/gallery/video/${media.fileName}`;
    //Checks if media.src exists as some pages send blobs instead or urls
    return (
      <video
        className="centerVideo"
        src={media.src ? media.src : src}
        controls={renderVideoControls}
      />
    );
  };

  renderMedia = () => {
    const { media, renderVideoControls } = this.props;

    if (media.mediaType === "video") {
      return this.renderVideo(media, renderVideoControls);
    }
    return this.renderImage(media);
  };

  render() {
    const { onClick, media } = this.props;

    return (
      <div className="imageBox" key={media.id} onClick={onClick}>
        {this.renderMedia()}
      </div>
    );
  }
}
export default Media;
