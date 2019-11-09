import React from "react";

export const renderErrorMessage = (message) => {
    return (
        <div className="three column">
            <div className="ui error center aligned header message">{message}</div>
        </div>
    )
};

export const renderPositiveMessage = (message) => {
    return (
        <div className="ui positive message">
            <div className="ui center aligned header">{message}</div>
        </div>
    )
};