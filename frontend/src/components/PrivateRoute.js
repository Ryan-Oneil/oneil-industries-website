import React from "react";
import {Redirect, Route} from 'react-router-dom';

export const PrivateRoute = ({isAuthenticated, user,  component: Component, ...rest}) => {
    return (
        <Route {...rest} render={props => (
            isAuthenticated ?
                <Component {...props} user={user} />
                : <Redirect to="/login" />
        )} />
    );
};