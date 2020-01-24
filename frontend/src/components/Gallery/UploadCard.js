import React from "react";
import Media from "./Media";

export default ({ value, onChange, media, onDelete, reachedUploadLimit }) => {
  return (
    <div className="ui card">
      <div className="image">
        <Media media={media} renderVideoControls />
        {reachedUploadLimit && (
          <div className="ui active dimmer">
            <div className="content">
              <h2 className="ui inverted icon header">
                <i className="ban icon" />
                This image exceeds your upload size limit
              </h2>
            </div>
          </div>
        )}
      </div>

      <div className="extra content">
        <div className="ui action input">
          <input type="text" defaultValue={value} onChange={onChange} />
          <button className="ui icon button" onClick={onDelete}>
            <i className="trash icon" />
          </button>
        </div>
      </div>
    </div>
  );
};
