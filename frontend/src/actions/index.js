import axios from 'axios';

export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'LOGIN_FAILURE';
export const LOGOUT_REQUEST = 'LOGOUT_REQUEST';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';
export const LOGOUT_FAILURE = 'LOGOUT_FAILURE';

export const MEDIA_REQUEST = 'MEDIA_REQUEST';
export const MEDIA_SUCCESS = 'MEDIA_SUCCESS';
export const MEDIA_FAILURE = 'MEDIA_FAILURE';

const API_URL = "http://localhost:8080";

function requestLogin(creds) {
    return {
        type: LOGIN_REQUEST,
        isFetching: true,
        isAuthenticated: false,
        creds
    }
}

function receiveLogin(token) {
    return {
        type: LOGIN_SUCCESS,
        isFetching: false,
        isAuthenticated: true,
        token: token
    }
}

function loginError(message) {
    return {
        type: LOGIN_FAILURE,
        isFetching: false,
        isAuthenticated: false,
        message
    }
}

function requestLogout() {
    return {
        type: LOGOUT_REQUEST,
        isFetching: true,
        isAuthenticated: true
    }
}

function receiveLogout() {
    return {
        type: LOGOUT_SUCCESS,
        isFetching: false,
        isAuthenticated: false
    }
}

export function loginUser(creds) {

    let config = {
        mode: "cors",
        method: 'POST',
        headers: { 'Content-Type':'application/json' },
        body: JSON.stringify({username: creds.username, password: creds.password})
    };

    return dispatch => {
        // We dispatch requestLogin to kickoff the call to the API
        dispatch(requestLogin(creds));

        return fetch(API_URL + '/login', config)
            .then(( response, user ) =>  {
                if (response.status > 400) {
                    // If there was a problem, we want to
                    // dispatch the error condition
                    dispatch(loginError("Invalid Login Details"));
                    return Promise.reject(user)
                } else {
                    // If login was successful, set the token in local storage
                    const token = response.headers.get("Authorization");
                    localStorage.setItem('token', token);
                    // Dispatch the success action
                    dispatch(receiveLogin(token))
                }
            }).catch(err => console.log("Error: ", err))
    }
}

export function logoutUser() {
    return dispatch => {
        dispatch(requestLogout());
        localStorage.removeItem('token');
        dispatch(receiveLogout());
    }
}

export const fetchImages = () => async dispatch => {
    await axios.create({
        baseURL: "http://localhost:8080/api"
    }).get("/gallery/medias").then( response => {
        dispatch({type: MEDIA_REQUEST, payload: response.data});
    }).catch(error => {
            dispatch({type: MEDIA_FAILURE, message: error});
        }
    );
};
