import {
  apiDeleteCall,
  apiGetCall,
  apiPostCall,
  apiPutCall
} from "../apis/api";
import {
  ALBUM_EDIT_PUT,
  ALBUM_FAILURE,
  ALBUM_REQUEST,
  MEDIA_DELETE_DONE,
  MEDIA_DELETE_FAIL,
  MEDIA_FAILURE,
  MEDIA_POST_SENT,
  MEDIA_POST_SUCCESS,
  MEDIA_REQUEST,
  MEDIA_UPDATE_SENT,
  USER_MEDIA_REQUEST
} from "./types";
import { SubmissionError } from "redux-form";
export * from "./authenication";
export * from "./contact";

export const fetchImages = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: MEDIA_REQUEST, payload: response.data });
    })
    .catch(error => {
      dispatch({ type: MEDIA_FAILURE, message: error });
    });
};

export const fetchUserImages = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: USER_MEDIA_REQUEST, payload: response.data });
    })
    .catch(error => {
      dispatch({ type: MEDIA_FAILURE, message: error });
    });
};

export const fetchAlbums = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: ALBUM_REQUEST, payload: response.data });
    })
    .catch(error => {
      dispatch({ type: ALBUM_FAILURE, message: error });
    });
};

export const updateAlbum = (endpoint, payload) => dispatch => {
  return new Promise((resolve, reject) => {
    apiPutCall(endpoint, payload)
      .then(response => {
        dispatch({ type: ALBUM_EDIT_PUT, album: response.data });
        resolve(response);
      })
      .catch(error => {
        dispatch({ type: ALBUM_FAILURE, message: error });
        reject(error);
      });
  });
};

export const uploadMedia = (endpoint, data) => dispatch => {
  let postData = new FormData();

  postData.append("file", data.file[0]);

  const options = {
    params: { name: data.name, privacy: data.linkStatus, albumName: data.album }
  };

  return apiPostCall(endpoint, postData, options)
    .then(response => {
      dispatch({ type: MEDIA_POST_SENT, message: response.data });
    })
    .catch(error => {
      if (error.response) {
        throw new SubmissionError({ _error: error.response.data });
      } else {
        throw new SubmissionError({ _error: error.message });
      }
    })
    .finally(dispatch({ type: MEDIA_POST_SUCCESS }));
};

export const deleteMedia = (endpoint, mediaID) => dispatch => {
  apiDeleteCall(endpoint)
    .then(response => {
      dispatch({
        type: MEDIA_DELETE_DONE,
        message: response.data,
        mediaDeleteID: mediaID
      });
    })
    .catch(error => {
      dispatch({ type: MEDIA_DELETE_FAIL, message: error.message });
    });
};

export const updateMedia = (endpoint, data) => dispatch => {
  return apiPutCall(endpoint, data)
    .then(response => {
      dispatch({ type: MEDIA_UPDATE_SENT, media: response.data });
    })
    .catch(error => {
      if (error.response) {
        throw new SubmissionError({ _error: error.response.data });
      } else {
        throw new SubmissionError({ _error: error.message });
      }
    });
};
