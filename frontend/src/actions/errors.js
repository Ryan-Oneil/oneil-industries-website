import { CLEAR_ERROR, DISABLE_ERRORS, SET_ERROR } from "./types";

export const setError = error => {
  return {
    type: SET_ERROR,
    error
  };
};

export const clearError = () => dispatch => {
  dispatch({
    type: CLEAR_ERROR
  });
};

export const disableError = () => dispatch => {
  dispatch({
    type: DISABLE_ERRORS
  });
};
