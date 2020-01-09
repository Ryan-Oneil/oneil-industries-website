import { apiGetCall, apiPutCall } from "../apis/api";
import {
  ADMIN_GET_USERS_FAIL,
  ADMIN_GET_USERS,
  ADMIN_GET_PENDING_PUBLIC_MEDIA_APPROVALS,
  ADMIN_APPROVE_PUBLIC_MEDIA,
  ADMIN_DENY_PUBLIC_MEDIA
} from "./types";
import { ADMIN_GET_ROLES } from "./types";
import { ADMIN_GET_USER_DETAIL } from "./types";
import { ADMIN_GET_USER_DETAIL_FAIL } from "./types";
import { ADMIN_UPDATE_USER_DETAILS } from "./types";
import { ADMIN_UPDATE_USER_QUOTA } from "./types";
import { SubmissionError } from "redux-form";

export const getUsers = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: ADMIN_GET_USERS, payload: response.data });
    })
    .catch(error => {
      if (error.response) {
        dispatch({
          type: ADMIN_GET_USERS_FAIL,
          errorMessage: error.response.data
        });
      } else {
        dispatch({ type: ADMIN_GET_USERS_FAIL, errorMessage: error.message });
      }
    });
};

export const getRoles = endpoint => dispatch => {
  apiGetCall(endpoint).then(response => {
    dispatch({ type: ADMIN_GET_ROLES, payload: response.data });
  });
};
export const getUserDetail = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: ADMIN_GET_USER_DETAIL, payload: response.data });
    })
    .catch(error => {
      if (error.response) {
        dispatch({
          type: ADMIN_GET_USER_DETAIL_FAIL,
          errorMessage: error.response.data
        });
      } else {
        dispatch({
          type: ADMIN_GET_USER_DETAIL_FAIL,
          errorMessage: error.message
        });
      }
    });
};

export const updateUserDetails = (endpoint, data, user) => dispatch => {
  const updatedUser = {
    username: user,
    password: data.password,
    email: data.email,
    role: data.role,
    enabled: data.enabled
  };

  return apiPutCall(endpoint, updatedUser)
    .then(() => {
      dispatch({ type: ADMIN_UPDATE_USER_DETAILS, user: updatedUser });
    })
    .catch(error => {
      if (error.response) {
        throw new SubmissionError({ _error: error.response.data });
      } else {
        throw new SubmissionError({ _error: error.message });
      }
    });
};

export const updateUserQuota = (endpoint, data) => dispatch => {
  apiPutCall(endpoint, data)
    .then(() => {
      dispatch({ type: ADMIN_UPDATE_USER_QUOTA, quota: data });
    })
    .catch(error => {
      if (error.response) {
        throw new SubmissionError({ _error: error.response.data });
      } else {
        throw new SubmissionError({ _error: error.message });
      }
    });
};

export const getMediaApprovals = endpoint => dispatch => {
  apiGetCall(endpoint).then(response => {
    dispatch({
      type: ADMIN_GET_PENDING_PUBLIC_MEDIA_APPROVALS,
      payload: response.data
    });
  });
};

export const approvePublicMedia = (endpoint, mediaApprovalID) => dispatch => {
  apiPutCall(endpoint).then(() => {
    dispatch({ type: ADMIN_APPROVE_PUBLIC_MEDIA, approvalID: mediaApprovalID });
  });
};

export const denyPublicMedia = (endpoint, mediaApprovalID) => dispatch => {
  apiPutCall(endpoint).then(() => {
    dispatch({ type: ADMIN_DENY_PUBLIC_MEDIA, approvalID: mediaApprovalID });
  });
};
