import {
  apiDeleteCall,
  apiGetCall,
  apiPostCall,
  apiPutCall
} from "../apis/api";
import {
  ALBUM_EDIT_PUT,
  ALBUM_REQUEST,
  MEDIA_DELETE_DONE,
  MEDIA_DELETE_FAIL,
  MEDIA_POST_SENT,
  MEDIA_POST_SUCCESS,
  MEDIA_REQUEST,
  MEDIA_RESET_MESSAGES,
  MEDIA_UPDATE_SENT,
  SET_ACTIVE_MEDIA_MODAL
} from "./types";
import { SubmissionError } from "redux-form";
import { setError } from "./errors";
export * from "./authenication";
export * from "./contact";

export const fetchImages = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: MEDIA_REQUEST, payload: response.data });
    })
    .catch(error => {
      if (error.response) {
        dispatch(setError(error.response.data));
      } else {
        dispatch(setError(error.message));
      }
    });
};

export const fetchAlbums = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: ALBUM_REQUEST, payload: response.data });
    })
    .catch(error => {
      if (error.response) {
        dispatch(setError(error.response.data));
      } else {
        dispatch(setError(error.message));
      }
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

export const updateMedia = (endpoint, data, mediaID) => dispatch => {
  const updatedMedia = {
    id: mediaID,
    name: data.name,
    linkStatus: data.privacy
  };

  return apiPutCall(endpoint, data)
    .then(response => {
      dispatch({
        type: MEDIA_UPDATE_SENT,
        media: updatedMedia,
        message: response.data
      });
    })
    .catch(error => {
      if (error.response) {
        throw new SubmissionError({ _error: error.response.data });
      } else {
        throw new SubmissionError({ _error: error.message });
      }
    });
};

export const clearMessages = () => {
  return {
    type: MEDIA_RESET_MESSAGES
  };
};

//Sets active media from the global media list
export const setActiveMediaForModal = mediaID => {
  return {
    type: SET_ACTIVE_MEDIA_MODAL,
    mediaID
  };
};
