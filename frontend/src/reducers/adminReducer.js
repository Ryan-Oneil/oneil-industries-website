import { createSlice } from "@reduxjs/toolkit";
import { apiGetCall, apiPutCall } from "../apis/api";
import { getApiError } from "../helpers";
import { setError } from "./globalErrorReducer";
import {
  ADMIN_GET_PENDING_APPROVALS_ENDPOINT,
  ADMIN_GET_ROLES,
  ADMIN_GET_STATS_ENDPOINT,
  ADMIN_GET_USERS_ENDPOINT
} from "../apis/endpoints";
import { normalize, schema } from "normalizr";

export const getAllUsers = () => dispatch => {
  return apiGetCall(ADMIN_GET_USERS_ENDPOINT)
    .then(({ data }) => {
      dispatch(fetchedUsers(data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getRoles = () => dispatch => {
  apiGetCall(ADMIN_GET_ROLES)
    .then(({ data }) => dispatch(fetchedRoles(data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const updateUser = user => dispatch => {
  const userDetails = { email: user.userEmail, password: user.userPass };

  return apiPutCall(
    `/user/${user.username}/details/update`,
    userDetails
  ).then(() =>
    dispatch(
      updateUserDetails({ name: user.username, email: userDetails.email })
    )
  );
};

export const updateUserRole = (username, role) => dispatch => {
  return apiPutCall(`/user/${username}/details/update`, { role }).then(() =>
    dispatch(updateUserDetails({ name: username, role }))
  );
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
export const changeUserPassword = (username, password) => {
  return apiPutCall(`/user/${username}/details/update`, password);
};

const getUserDetailsBase = username => {
  return apiGetCall(`/user/${username}/details`);
};

export const adminGetUserDetails = username => dispatch => {
  return getUserDetailsBase(username)
    .then(response => dispatch(getUserDetails(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getUserFileStats = username => dispatch => {
  return apiGetCall(`/user/${username}/links/stats`)
    .then(({ data }) => dispatch(getUserFileShareStats(data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getAdminLinkStats = () => dispatch => {
  return apiGetCall("/admin/link/stats")
    .then(response => dispatch(getFileShareStats(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const updateUserQuota = (username, values) => dispatch => {
  return apiPutCall(
    `/user/admin/user/${username}/update/quota`,
    values
  ).then(() => dispatch(updateUserDetails({ name: username, quota: values })));
};

export const updateUserAccountStatus = (username, status) => dispatch => {
  let endpoint = status ? "enable" : "disable";

  return apiPutCall(`/user/admin/user/${username}/${endpoint}`)
    .then(() =>
      dispatch(updateUserDetails({ name: username, enabled: status }))
    )
    .catch(error => dispatch(setError(getApiError(error))));
};

const user = new schema.Entity("user", {}, { idAttribute: "name" });
const userList = new schema.Array(user);

export const slice = createSlice({
  name: "admin",
  initialState: {
    roles: [],
    mediaApprovals: [],
    entities: {
      users: {}
    },
    fileShare: {
      totalViews: 0,
      totalLinks: 0,
      mostViewed: [],
      recentShared: []
    },
    stats: {
      remainingStorage: 0,
      totalMedia: 0,
      totalAlbums: 0,
      totalUsers: 0,
      usedStorage: 0,
      recentUsers: []
    },
    userStats: {
      totalViews: 0,
      totalLinks: 0
    }
  },
  reducers: {
    fetchedStats(state, action) {
      state.stats = action.payload;
    },
    fetchedUsers(state, action) {
      const users = normalize(action.payload, userList);

      state.entities.users = users.entities.user;
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
    },
    getUserDetails(state, action) {
      state.entities.users[action.payload.name] = {
        ...state.entities.users[action.payload.name],
        ...action.payload
      };
    },
    updateUserDetails(state, action) {
      const user = state.entities.users[action.payload.name];
      state.entities.users[action.payload.name] = {
        ...user,
        ...action.payload
      };
    },
    getFileShareStats(state, action) {
      state.fileShare = action.payload;
    },
    getUserFileShareStats(state, action) {
      state.userStats = action.payload;
    }
  }
});
export default slice.reducer;
export const {
  fetchedStats,
  fetchedUsers,
  fetchedMediaApprovals,
  removedMediaApproval,
  fetchedRoles,
  getUserDetails,
  updateUserDetails,
  getFileShareStats,
  getUserFileShareStats
} = slice.actions;
