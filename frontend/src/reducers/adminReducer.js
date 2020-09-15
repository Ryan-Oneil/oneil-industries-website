import { createSlice } from "@reduxjs/toolkit";
import { apiGetCall, apiPutCall } from "../apis/api";
import {
  ADMIN_GET_USER_DETAIL,
  ADMIN_UPDATE_USER_DETAILS,
  ADMIN_UPDATE_USER_QUOTA
} from "../actions/types";
import { getApiError } from "../helpers";
import { setError } from "./globalErrorReducer";
import {
  ADMIN_GET_PENDING_APPROVALS_ENDPOINT,
  ADMIN_GET_STATS_ENDPOINT,
  ADMIN_GET_USERS_ENDPOINT
} from "../apis/endpoints";

export const getAllUsers = () => dispatch => {
  return apiGetCall(ADMIN_GET_USERS_ENDPOINT)
    .then(({ data }) => {
      dispatch(fetchedUsers(data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getRoles = endpoint => dispatch => {
  apiGetCall(endpoint).then(({ data }) => dispatch(fetchedRoles(data)));
};
export const getUserDetail = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({ type: ADMIN_GET_USER_DETAIL, payload: response.data });
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const updateUserDetails = (endpoint, data, user) => dispatch => {
  const updatedUser = {
    username: user,
    password: data.password,
    email: data.email,
    role: data.role,
    enabled: data.enabled
  };

  return apiPutCall(endpoint, updatedUser).then(() => {
    dispatch({ type: ADMIN_UPDATE_USER_DETAILS, user: updatedUser });
  });
};

export const updateUserQuota = (endpoint, data) => dispatch => {
  apiPutCall(endpoint, data).then(() => {
    dispatch({ type: ADMIN_UPDATE_USER_QUOTA, quota: data });
  });
};

export const getMediaApprovals = () => dispatch => {
  return apiGetCall(ADMIN_GET_PENDING_APPROVALS_ENDPOINT)
    .then(({ data }) => {
      dispatch(fetchedMediaApprovals(data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const approvePublicMedia = (endpoint, mediaApprovalID) => dispatch => {
  apiPutCall(endpoint).then(() => {
    dispatch(removedMediaApproval(mediaApprovalID));
  });
};

export const denyPublicMedia = (endpoint, mediaApprovalID) => dispatch => {
  apiPutCall(endpoint).then(() => {
    dispatch(removedMediaApproval(mediaApprovalID));
  });
};

export const getAdminStats = () => dispatch => {
  return apiGetCall(ADMIN_GET_STATS_ENDPOINT)
    .then(({ data }) => {
      dispatch(fetchedStats(data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const slice = createSlice({
  name: "admin",
  initialState: {
    roles: [],
    users: [],
    mediaApprovals: [],
    stats: {
      remainingStorage: 0,
      totalMedia: 0,
      totalAlbums: 0,
      totalUsers: 0,
      usedStorage: 0,
      recentUsers: []
    }
  },
  reducers: {
    fetchedStats(state, action) {
      state.stats = action.payload;
    },
    fetchedUsers(state, action) {
      state.users = action.payload;
    },
    fetchedMediaApprovals(state, action) {
      state.mediaApprovals = action.payload;
    },
    removedMediaApproval(state, action) {
      state.mediaApprovals = state.mediaApprovals.filter(
        mediaApproval => mediaApproval.id !== action.payload
      );
    },
    fetchedRoles(state, action) {
      state.roles = action.payload;
    }
  }
});
export default slice.reducer;
export const {
  fetchedStats,
  fetchedUsers,
  fetchedMediaApprovals,
  removedMediaApproval,
  fetchedRoles
} = slice.actions;
