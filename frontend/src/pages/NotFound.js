import React from "react";
import {useLocation} from "react-router-dom";
export default () => {
    let location = useLocation();

    return (
        <div className="marginPadding">
            <h1 className="ui center aligned header">404 - {location.pathname} doesn't exist</h1>
        </div>
    );
}