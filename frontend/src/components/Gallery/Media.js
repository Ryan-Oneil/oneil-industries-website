import React from "react";
import { Image } from "antd";
import { MEDIA_IMAGE_URL, MEDIA_VIDEO_URL } from "../../apis/endpoints";
import noImage from "../../assets/images/noimage.png";
import playIcon from "../../assets/images/play-icon.jpg";

export default ({
  fullSize,
  showVideoPlayButton,
  showPreview,
  fileName,
  mediaType,
}) => {
  const renderImage = () => {
    const endpoint = `${fullSize ? "" : "thumbnail/"}${fileName}`;
    const previewConfig = {
      src: `${MEDIA_IMAGE_URL}${fileName}`,
    };
    return (
      <Image
        alt={"User uploaded image"}
        src={MEDIA_IMAGE_URL + endpoint}
        preview={showPreview ? previewConfig : false}
        fallback={noImage}
      />
    );
  };

  const renderVideo = () => {
    const src = `${MEDIA_VIDEO_URL}${fileName}`;

    if (showVideoPlayButton) {
      return (
        <div className={"videoMedia"}>
          {renderImage()}
          <div className={"playButton"}>
            <img src={playIcon} alt={"Play video icon"} />
          </div>
        </div>
      );
    }
    return (
      <video
        className="centerVideo"
        src={src}
        style={{ width: "100%", height: "100%" }}
        controls={true}
        muted
      />
    );
  };

  const renderMedia = () => {
    if (mediaType.toLowerCase() === "video") {
      return renderVideo();
    }
    return renderImage();
  };
  return renderMedia();
};
