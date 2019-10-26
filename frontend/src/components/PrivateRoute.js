import React from "react";
import {Redirect, Route} from 'react-router-dom';

export const PrivateRoute = ( {isAuthenticated, ...props}) => isAuthenticated ? <Route {...props}/> : <Redirect to="/login"/>;