import React from "react";
import { Image } from "antd";
import { MEDIA_IMAGE_URL, MEDIA_VIDEO_URL } from "../../apis/endpoints";

export default ({
  media,
  fullSize,
  renderVideoControls,
  showVideoPlayButton,
  showPreview
}) => {
  const renderImage = media => {
    const endpoint = `${fullSize ? "" : "thumbnail/"}${media.fileName}`;
    const previewConfig = {
      src: `${MEDIA_IMAGE_URL}${media.fileName}`
    };
    return (
      <Image
        alt={media.name}
        src={MEDIA_IMAGE_URL + endpoint}
        preview={showPreview ? previewConfig : false}
        style={{ margin: "auto" }}
        width={"100%"}
      />
    );
  };

  const renderVideo = (media, renderVideoControls) => {
    const src = `${MEDIA_VIDEO_URL}${media.fileName}`;

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

  const renderMedia = () => {
    if (media.mediaType === "video") {
      return renderVideo(media, renderVideoControls);
    }
    return renderImage(media);
  };

  const renderMissingMedia = () => {
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
    return renderMissingMedia();
  }
  return renderMedia();
};
