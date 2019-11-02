import {renderErrorMessage} from "../../../ErrorMessage";
import Media from "./Media";
import React from "react";

export default (mediasList, message, mediaOnClick) => {
    if (message) {
        return renderErrorMessage(message);
    }
    if (mediasList) {
        return mediasList.map(media => {
            return (
                <div className="column" key={media.id} >
                    <div className="ui card">
                        <Media media={media} onClick={mediaOnClick.bind(this, media)}/>
                        <h1 className="ui center aligned header">{media.name}</h1>
                    </div>
                </div>
            );
        })
    }
}