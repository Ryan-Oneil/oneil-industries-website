import {apiDeleteCall, apiGetCall, apiPostCall, apiPutCall} from "../apis/api";

export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'LOGIN_FAILURE';
export const LOGOUT_REQUEST = 'LOGOUT_REQUEST';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';
export const LOGOUT_FAILURE = 'LOGOUT_FAILURE';

export const MEDIA_REQUEST = 'MEDIA_REQUEST';
export const USER_MEDIA_REQUEST = 'USER_MEDIA_REQUEST';
export const MEDIA_SUCCESS = 'MEDIA_SUCCESS';
export const MEDIA_FAILURE = 'MEDIA_FAILURE';
export const MEDIA_POST_SENT = 'MEDIA_POST_SENT';
export const MEDIA_POST_FAIL = 'MEDIA_POST_FAIL';
export const MEDIA_POST_SUCCESS = 'MEDIA_POST_SUCCESS';
export const MEDIA_DELETE_FAIL = 'MEDIA_DELETE_FAIL';
export const MEDIA_DELETE_DONE = 'MEDIA_DELETE_DONE';

export const ALBUM_REQUEST = 'ALBUM_REQUEST';
export const ALBUM_FAILURE = 'ALBUM_FAILURE';
export const ALBUM_EDIT_PUT = 'ALBUM_EDIT_PUT';

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
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username: creds.username, password: creds.password})
    };

    return dispatch => {
        // We dispatch requestLogin to kickoff the call to the API
        dispatch(requestLogin(creds));

        return fetch(API_URL + '/login', config)
            .then((response, user) => {
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

export const fetchImages = (endpoint) => dispatch => {
    apiGetCall(endpoint).then(response => {
        dispatch({type: MEDIA_REQUEST, payload: response.data});
    }).catch(error => {
            dispatch({type: MEDIA_FAILURE, message: error});
        }
    );
};

export const fetchUserImages = (endpoint) => dispatch => {
    apiGetCall(endpoint).then(response => {
        dispatch({type: USER_MEDIA_REQUEST, payload: response.data});
    }).catch(error => {
            dispatch({type: MEDIA_FAILURE, message: error});
        }
    );
};

export const fetchAlbum = (endpoint) => dispatch => {
    apiGetCall(endpoint).then(response => {
        dispatch({type: ALBUM_REQUEST, payload: response.data});
    }).catch(error => {
            dispatch({type: ALBUM_FAILURE, message: error});
        }
    );
};

export const updateAlbum = (endpoint, payload) => dispatch => {
    apiPutCall(endpoint, payload).then(response => {
        dispatch({type: ALBUM_EDIT_PUT, album: response.data});
    }).catch(error => {
            dispatch({type: ALBUM_FAILURE, message: error});
        }
    );
};

export const uploadMedia = (endpoint, data) => dispatch => {
    let postData = new FormData();

    postData.append("file", data.file[0]);
    postData.append("name", data.name);
    postData.append("privacy", data.linkStatus);
    postData.append("albumName", data.album);

    apiPostCall(endpoint, postData).then(response => {
        dispatch({type: MEDIA_POST_SENT, message: response.data})
    }).catch(error => {
        dispatch({type: MEDIA_POST_FAIL, message: error.message})
    }).finally(dispatch({type: MEDIA_POST_SUCCESS}));
};

export const deleteMedia = (endpoint, mediaID) => dispatch => {
    apiDeleteCall(endpoint).then(response => {
        dispatch({type: MEDIA_DELETE_DONE, message: response.data, mediaDeleteID: mediaID})
    }).catch(error => {
        dispatch({type: MEDIA_DELETE_FAIL, message: error.message})
    })
};