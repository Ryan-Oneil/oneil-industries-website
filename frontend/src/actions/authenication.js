import {
    LOGIN_FAILURE,
    LOGIN_REQUEST,
    LOGIN_SUCCESS,
    LOGOUT_REQUEST,
    LOGOUT_SUCCESS, REGISTER_FAIL, REGISTER_SUCCESS, RESET_PASSWORD_FAIL, RESET_PASSWORD_SENT
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
            const token = response.headers['authorization'];

            localStorage.setItem('token', token);
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

export const registerUser = (creds) => dispatch => {
    return new Promise((resolve, reject) => {
        apiPostCall(API_URL + '/api/auth/register', creds).then(response => {
            dispatch({type: REGISTER_SUCCESS, message: response.data});
            resolve(response);
        }).catch(error => {
            dispatch({type: REGISTER_FAIL, errorMessage: error.message});
            reject(error);
        })
    })
};

export function logoutUser() {
    return dispatch => {
        dispatch(requestLogout());
        localStorage.removeItem('token');
        dispatch(receiveLogout());
    }
}

export const resetPassword = (email) => dispatch => {
    return new Promise((resolve, reject) => {
        apiPostCall(API_URL + '/api/auth/forgotPassword/'+ email).then(response => {
            dispatch({type: RESET_PASSWORD_SENT, message: response.data});
            resolve(response);
        }).catch((error) => {
            if (error.response) {
                dispatch({type: RESET_PASSWORD_FAIL, errorMessage: error.response.data});
            } else {
                dispatch({type: RESET_PASSWORD_FAIL, errorMessage: error.message});
            }
            reject(error);
        })
    })
};