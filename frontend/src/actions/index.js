import {apiDeleteCall, apiGetCall, apiPostCall, apiPutCall} from "../apis/api";
import {
    ALBUM_EDIT_PUT,
    ALBUM_FAILURE,
    ALBUM_REQUEST,
    MEDIA_DELETE_DONE, MEDIA_DELETE_FAIL,
    MEDIA_FAILURE, MEDIA_POST_FAIL, MEDIA_POST_SENT, MEDIA_POST_SUCCESS,
    MEDIA_REQUEST, MEDIA_UPDATE_FAIL, MEDIA_UPDATE_SENT, USER_MEDIA_REQUEST
} from "./types";
export * from './authenication';
export * from './contact';

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

export const fetchAlbums = (endpoint) => dispatch => {
    apiGetCall(endpoint).then(response => {
        dispatch({type: ALBUM_REQUEST, payload: response.data});
    }).catch(error => {
            dispatch({type: ALBUM_FAILURE, message: error});
        }
    );
};

export const updateAlbum = (endpoint, payload) => dispatch => {

    return new Promise((resolve, reject) => {
        apiPutCall(endpoint, payload).then(response => {
            dispatch({type: ALBUM_EDIT_PUT, album: response.data});
            resolve(response);
        }).catch(error => {
                dispatch({type: ALBUM_FAILURE, message: error});
                reject(error);
            }
        );
    });
};

export const uploadMedia = (endpoint, data) => dispatch => {
    let postData = new FormData();

    postData.append("file", data.file[0]);
    postData.append("name", data.name);
    postData.append("privacy", data.linkStatus);
    postData.append("albumName", data.album);

    return new Promise((resolve, reject) => {
        apiPostCall(endpoint, postData).then(response => {
            dispatch({type: MEDIA_POST_SENT, message: response.data});
            resolve(response);
        }).catch(error => {
            dispatch({type: MEDIA_POST_FAIL, message: error.message});
            reject(error.message);
        }).finally(dispatch({type: MEDIA_POST_SUCCESS}));
    });
};

export const deleteMedia = (endpoint, mediaID) => dispatch => {
    apiDeleteCall(endpoint).then(response => {
        dispatch({type: MEDIA_DELETE_DONE, message: response.data, mediaDeleteID: mediaID})
    }).catch(error => {
        dispatch({type: MEDIA_DELETE_FAIL, message: error.message})
    })
};

export const updateMedia = (endpoint, data) => dispatch => {
    return new Promise((resolve, reject) => {
        apiPutCall(endpoint, data).then(response => {
            dispatch({type: MEDIA_UPDATE_SENT, media: response.data});
            resolve(response);
        }).catch(error => {
            dispatch({type: MEDIA_UPDATE_FAIL, message: error.message});
            reject(error.message);
        })
    });
};