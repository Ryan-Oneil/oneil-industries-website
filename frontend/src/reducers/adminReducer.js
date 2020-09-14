import { createSlice } from "@reduxjs/toolkit";
import { apiGetCall, apiPutCall } from "../apis/api";
import {
  ADMIN_APPROVE_PUBLIC_MEDIA,
  ADMIN_DENY_PUBLIC_MEDIA,
  ADMIN_GET_PENDING_PUBLIC_MEDIA_APPROVALS,
  ADMIN_GET_ROLES,
  ADMIN_GET_STATS,
  ADMIN_GET_USER_DETAIL,
  ADMIN_GET_USERS,
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

// export default (
//   state = {
//     roles: [],
//     users: [],
//     user: "",
//     mediaApprovals: [],
//     message: "",
//     stats: {
//       remainingStorage: 0,
//       totalMedia: 0,
//       totalAlbums: 0,
//       totalUsers: 0,
//       recentUsers: [],
//       feedback: []
//     }
//   },
//   action
// ) => {
//   switch (action.type) {
//     case ADMIN_GET_USERS: {
//       return {
//         ...state,
//         users: action.payload.filter(user => {
//           user.role = user.role.replace("ROLE_", "");
//           return user;
//         })
//       };
//     }
//     case ADMIN_GET_ROLES: {
//       return { ...state, roles: action.payload };
//     }
//     case ADMIN_GET_USER_DETAIL: {
//       action.payload.details.role = action.payload.details.role.replace(
//         "ROLE_",
//         ""
//       );
//       return { ...state, user: action.payload };
//     }
//     case ADMIN_UPDATE_USER_DETAILS: {
//       return { ...state, user: { ...state.user, details: action.user } };
//     }
//     case ADMIN_UPDATE_USER_QUOTA: {
//       return {
//         ...state,
//         user: {
//           ...state.user,
//           storageQuota: {
//             ...state.user.storageQuota,
//             max: action.quota.max,
//             ignoreQuota: action.quota.ignoreQuota
//           }
//         }
//       };
//     }
//     case ADMIN_GET_PENDING_PUBLIC_MEDIA_APPROVALS: {
//       return { ...state, mediaApprovals: action.payload };
//     }
//     case ADMIN_DENY_PUBLIC_MEDIA:
//     case ADMIN_APPROVE_PUBLIC_MEDIA: {
//       return {
//         ...state,
//         mediaApprovals: filterApprovals(action.approvalID, state.mediaApprovals)
//       };
//     }
//     case ADMIN_GET_STATS: {
//       return { ...state, stats: action.payload };
//     }
//     default: {
//       return state;
//     }
//   }
// };

export const getAllUsers = () => dispatch => {
  return apiGetCall(ADMIN_GET_USERS_ENDPOINT)
    .then(({ data }) => {
      dispatch(getUsers(data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
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
  apiGetCall(ADMIN_GET_STATS_ENDPOINT)
    .then(({ data }) => {
      dispatch(getStats(data));
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
    getStats(state, action) {
      state.stats = action.payload;
    },
    getUsers(state, action) {
      state.users = action.payload;
    },
    fetchedMediaApprovals(state, action) {
      state.mediaApprovals = action.payload;
    },
    removedMediaApproval(state, action) {
      state.mediaApprovals = state.mediaApprovals.filter(
        mediaApproval => mediaApproval.id !== action.payload
      );
    }
  }
});
export default slice.reducer;
export const {
  getStats,
  getUsers,
  fetchedMediaApprovals,
  removedMediaApproval
} = slice.actions;
