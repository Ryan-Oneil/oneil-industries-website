import {
    ADMIN_GET_ROLES,
    ADMIN_GET_USER_DETAIL,
    ADMIN_GET_USER_DETAIL_FAIL,
    ADMIN_GET_USERS,
    ADMIN_GET_USERS_FAIL, ADMIN_UPDATE_USER_DETAILS, ADMIN_UPDATE_USER_QUOTA
} from "../actions/types";

export default (state = {
    roles: [],
    users: [],
    user:"",
    errorMessage: "",
    message: ""
}, action) => {
    switch (action.type) {
        case ADMIN_GET_USERS: {
            return {...state, users: action.payload, errorMessage: ""};
        }
        case ADMIN_GET_USERS_FAIL: {
            return {...state, errorMessage: action.errorMessage};
        }
        case ADMIN_GET_ROLES: {
            return {...state, roles: action.payload, errorMessage: ""};
        }
        case ADMIN_GET_USER_DETAIL: {
            return {...state, user: action.payload, errorMessage: ""};
        }
        case ADMIN_GET_USER_DETAIL_FAIL: {
            return {...state, errorMessage: action.errorMessage};
        }
        case ADMIN_UPDATE_USER_DETAILS: {
            return {...state, user: {...state.user, details: action.user}};
        }
        case ADMIN_UPDATE_USER_QUOTA: {
            return {...state, user: {...state.user, storageQuota:{ ...state.user.storageQuota, max: action.quota.max, ignoreQuota: action.quota.ignoreQuota}}};
        }
        default: {
            return state;
        }
    }
};