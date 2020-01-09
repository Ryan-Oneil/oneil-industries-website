import {
  ADMIN_APPROVE_PUBLIC_MEDIA,
  ADMIN_DENY_PUBLIC_MEDIA,
  ADMIN_GET_PENDING_PUBLIC_MEDIA_APPROVALS,
  ADMIN_GET_ROLES,
  ADMIN_GET_USER_DETAIL,
  ADMIN_GET_USER_DETAIL_FAIL,
  ADMIN_GET_USERS,
  ADMIN_GET_USERS_FAIL,
  ADMIN_UPDATE_USER_DETAILS,
  ADMIN_UPDATE_USER_QUOTA
} from "../actions/types";

export default (
  state = {
    roles: [],
    users: [],
    user: "",
    mediaApprovals: [],
    errorMessage: "",
    message: ""
  },
  action
) => {
  switch (action.type) {
    case ADMIN_GET_USERS: {
      return {
        ...state,
        users: action.payload.filter(user => {
          user.role = user.role.replace("ROLE_", "");
          return user;
        }),
        errorMessage: ""
      };
    }
    case ADMIN_GET_USERS_FAIL: {
      return { ...state, errorMessage: action.errorMessage };
    }
    case ADMIN_GET_ROLES: {
      return { ...state, roles: action.payload, errorMessage: "" };
    }
    case ADMIN_GET_USER_DETAIL: {
      action.payload.details.role = action.payload.details.role.replace(
        "ROLE_",
        ""
      );
      return { ...state, user: action.payload, errorMessage: "" };
    }
    case ADMIN_GET_USER_DETAIL_FAIL: {
      return { ...state, errorMessage: action.errorMessage };
    }
    case ADMIN_UPDATE_USER_DETAILS: {
      return { ...state, user: { ...state.user, details: action.user } };
    }
    case ADMIN_UPDATE_USER_QUOTA: {
      return {
        ...state,
        user: {
          ...state.user,
          storageQuota: {
            ...state.user.storageQuota,
            max: action.quota.max,
            ignoreQuota: action.quota.ignoreQuota
          }
        }
      };
    }
    case ADMIN_GET_PENDING_PUBLIC_MEDIA_APPROVALS: {
      return { ...state, mediaApprovals: action.payload };
    }
    case ADMIN_DENY_PUBLIC_MEDIA:
    case ADMIN_APPROVE_PUBLIC_MEDIA: {
      return {
        ...state,
        mediaApprovals: filterApprovals(action.approvalID, state.mediaApprovals)
      };
    }
    default: {
      return state;
    }
  }
};

const filterApprovals = (approvalID, state) => {
  return state.filter(approval => approval.id !== approvalID);
};
