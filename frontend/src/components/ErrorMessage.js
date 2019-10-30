import React from "react";

export const renderErrorMessage = (message) => {
    return (
        <div className="three column">
            <div className="ui error center aligned header">{message}</div>
        </div>
    )
};