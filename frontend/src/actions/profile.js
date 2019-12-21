import { apiDeleteCall, apiGetCall, apiPostCall } from "../apis/api";
import {
  UPDATE_USER_DETAILS_SENT,
  USER_PROFILE_GENERATE_API_TOKEN,
  USER_PROFILE_GENERATE_API_TOKEN_FAIL,
  USER_PROFILE_GENERATE_SHAREX_CONFIG,
  USER_PROFILE_GENERATE_SHAREX_CONFIG_FAIL,
  USER_PROFILE_GET,
  USER_PROFILE_GET_API_TOKENS,
  USER_PROFILE_GET_API_TOKENS_FAIL,
  USER_PROFILE_GET_FAIL,
  USER_PROFILE_GET_STORAGE_QUOTA,
  USER_PROFILE_GET_STORAGE_QUOTA_FAIL,
  USER_PROFILE_SERVICE_ADD_DISCORD,
  USER_PROFILE_SERVICE_ADD_TS,
  USER_PROFILE_SERVICE_DELETE_DISCORD,
  USER_PROFILE_SERVICE_DELETE_FAIL,
  USER_PROFILE_SERVICE_DELETE_TS,
  USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS,
  USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS_FAIL
} from "./types";
import { SubmissionError } from "redux-form";

export const getUserProfile = endpoint => dispatch => {
  return new Promise((resolve, reject) => {
    apiGetCall(endpoint)
      .then(response => {
        dispatch({ type: USER_PROFILE_GET, payload: response.data });
        resolve(response);
      })
      .catch(error => {
        if (error.response) {
          let errorMessage = error.response.data;

          if (errorMessage.message) {
            errorMessage = errorMessage.message;
          }
          dispatch({ type: USER_PROFILE_GET_FAIL, errorMessage: errorMessage });
        } else {
          dispatch({
            type: USER_PROFILE_GET_FAIL,
            errorMessage: error.message
          });
        }
        reject(error);
      });
  });
};

export const changeUserDetails = (details, endpoint) => dispatch => {
  return apiPostCall(endpoint, details)
    .then(() => {
      dispatch({
        type: UPDATE_USER_DETAILS_SENT,
        updatedDetails: details
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

export const getUnregisteredServiceClients = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({
        type: USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS,
        payload: response.data,
        errorMessage: ""
      });
    })
    .catch(error => {
      if (error.response) {
        dispatch({
          type: USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS_FAIL,
          errorMessage: error.response.data
        });
      } else {
        dispatch({
          type: USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS_FAIL,
          errorMessage: error.message
        });
      }
    });
};

export const addUserService = (
  endpoint,
  service,
  serviceClient
) => dispatch => {
  return apiPostCall(endpoint + service, serviceClient)
    .then(response => {
      if (service === "teamspeak") {
        dispatch({
          type: USER_PROFILE_SERVICE_ADD_TS,
          payload: response.data,
          errorMessage: ""
        });
      } else if (service === "discord") {
        dispatch({
          type: USER_PROFILE_SERVICE_ADD_DISCORD,
          payload: response.data,
          errorMessage: ""
        });
      }
    })
    .catch(error => {
      if (error.response) {
        throw new SubmissionError({ _error: error.response.data });
      } else {
        throw new SubmissionError({ _error: error.message });
      }
    });
};

export const deleteUserService = (endpoint, serviceID, service) => dispatch => {
  apiDeleteCall(endpoint + serviceID)
    .then(response => {
      if (service === "teamspeak") {
        dispatch({
          type: USER_PROFILE_SERVICE_DELETE_TS,
          message: response.data,
          errorMessage: "",
          serviceID
        });
      } else if (service === "discord") {
        dispatch({
          type: USER_PROFILE_SERVICE_DELETE_DISCORD,
          message: response.data,
          errorMessage: "",
          serviceID
        });
      }
    })
    .catch(error => {
      if (error.response) {
        dispatch({
          type: USER_PROFILE_SERVICE_DELETE_FAIL,
          errorMessage: error.response.data
        });
      } else {
        dispatch({
          type: USER_PROFILE_SERVICE_DELETE_FAIL,
          errorMessage: error.message
        });
      }
    });
};

export const getUserStorage = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({
        type: USER_PROFILE_GET_STORAGE_QUOTA,
        payload: response.data
      });
    })
    .catch(error => {
      if (error.response) {
        dispatch({
          type: USER_PROFILE_GET_STORAGE_QUOTA_FAIL,
          errorMessage: error.response.data
        });
      } else {
        dispatch({
          type: USER_PROFILE_GET_STORAGE_QUOTA_FAIL,
          errorMessage: error.message
        });
      }
    });
};

export const getAPIToken = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: USER_PROFILE_GET_API_TOKENS, payload: response.data });
    })
    .catch(error => {
      if (error.response) {
        dispatch({
          type: USER_PROFILE_GET_API_TOKENS_FAIL,
          errorMessage: error.response.data
        });
      } else {
        dispatch({
          type: USER_PROFILE_GET_API_TOKENS_FAIL,
          errorMessage: error.message
        });
      }
    });
};

export const generateShareXConfig = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({
        type: USER_PROFILE_GENERATE_SHAREX_CONFIG,
        payload: response.data
      });
    })
    .catch(error => {
      if (error.response) {
        dispatch({
          type: USER_PROFILE_GENERATE_SHAREX_CONFIG_FAIL,
          shareXError: error.response.data
        });
      } else {
        dispatch({
          type: USER_PROFILE_GENERATE_SHAREX_CONFIG_FAIL,
          shareXError: error.message
        });
      }
    });
};

export const generateAPIToken = endpoint => dispatch => {
  return apiGetCall(endpoint)
    .then(response => {
      dispatch({
        type: USER_PROFILE_GENERATE_API_TOKEN,
        payload: response.data
      });
    })
    .catch(error => {
      if (error.response) {
        dispatch({
          type: USER_PROFILE_GENERATE_API_TOKEN_FAIL,
          errorMessage: error.response.data
        });
      } else {
        dispatch({
          type: USER_PROFILE_GENERATE_API_TOKEN_FAIL,
          errorMessage: error.message
        });
      }
    });
};
