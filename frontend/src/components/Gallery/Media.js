import React from "react";
import { BASE_URL } from "../../apis/api";
import { Image } from "antd";

export default ({
  media,
  fullSize,
  renderVideoControls,
  showVideoPlayButton,
  showPreview
}) => {
  const renderImage = media => {
    const baseSrc = `${BASE_URL}/gallery/image`;
    const endpoint = `${fullSize ? "" : "/thumbnail"}/${media.fileName}`;
    const previewConfig = {
      src: `${BASE_URL}/gallery/image/${media.fileName}`
    };

    return (
      <Image
        alt={media.name}
        src={baseSrc + endpoint}
        preview={showPreview ? previewConfig : false}
        style={{ margin: "auto" }}
        width={"100%"}
      />
    );
  };

  const renderVideo = (media, renderVideoControls) => {
    const src = `${BASE_URL}/gallery/video/${media.fileName}`;

    return (
      <div className={"videoMedia"}>
        <video
          className="centerVideo"
          src={src}
          controls={renderVideoControls}
          style={{ width: "100%", height: "100%" }}
        />
        {showVideoPlayButton && (
          <div className={"playButton"}>
            <img
              src={require("../../assets/images/play-icon.jpg")}
              alt={"Play video icon"}
            />
          </div>
        )}
      </div>
    );
  };

  const RenderMedia = () => {
    if (media.mediaType === "video") {
      return renderVideo(media, renderVideoControls);
    }
    return renderImage(media);
  };

  const RenderMissingMedia = () => {
    return (
      <Image
        alt={"No media"}
        src={require("../../assets/images/noimage.png")}
        preview={false}
        style={{ margin: "auto" }}
        width={"100%"}
      />
    );
  };
  if (!media) {
    return <RenderMissingMedia />;
  }
  return <RenderMedia />;
};
