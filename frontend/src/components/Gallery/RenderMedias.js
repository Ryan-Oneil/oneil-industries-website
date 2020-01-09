import Media from "./Media";
import React from "react";

export default (mediasList, mediaOnClick, displayUploader) => {
  return mediasList.map(media => {
    return (
      <div className="column" key={media.id}>
        <div className="ui card">
          <Media media={media} onClick={mediaOnClick.bind(this, media.id)} />
          <div className="content centerText">
            {displayUploader && <div className="header">{media.uploader}</div>}
            {!displayUploader && <div className="header">{media.name}</div>}
            <div className="description">{media.dateAdded}</div>
          </div>
        </div>
      </div>
    );
  });
};
