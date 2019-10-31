import {
    LOGIN_FAILURE,
    LOGIN_REQUEST,
    LOGIN_SUCCESS,
    LOGOUT_REQUEST,
    LOGOUT_SUCCESS
} from "./types";
import {apiPostCall} from "../apis/api";

const API_URL = "http://localhost:8080";

function requestLogin(creds) {
    return {
        type: LOGIN_REQUEST,
        isAuthenticated: false,
        creds
    }
}

function receiveLogin(token) {
    return {
        type: LOGIN_SUCCESS,
        isAuthenticated: true,
        token: token
    }
}

function loginError(message) {
    return {
        type: LOGIN_FAILURE,
        isAuthenticated: false,
        message
    }
}

function requestLogout() {
    return {
        type: LOGOUT_REQUEST,
        isAuthenticated: true
    }
}

function receiveLogout() {
    return {
        type: LOGOUT_SUCCESS,
        isAuthenticated: false
    }
}

export const loginUser = (creds) => dispatch => {
    return new Promise((resolve, reject) => {
        apiPostCall(API_URL + '/login', creds).then(response => {
            dispatch(requestLogin(creds));
            // If login was successful, set the token in local storage
            const token = response.headers['authorization'];

            localStorage.setItem('token', token);
            // Dispatch the success action
            dispatch(receiveLogin(token));
            resolve(response);
        }).catch(error => {
                let message = error.message;

                if (error.response && error.response.status === 403) message = "Invalid login details";

                dispatch(loginError(message));
                reject(message);
            }
        );
    })
};

export function logoutUser() {
    return dispatch => {
        dispatch(requestLogout());
        localStorage.removeItem('token');
        dispatch(receiveLogout());
    }
}