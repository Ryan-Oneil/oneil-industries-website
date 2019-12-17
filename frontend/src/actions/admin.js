import {apiGetCall, apiPutCall} from "../apis/api";
import {} from "./types";
import {ADMIN_GET_USERS_FAIL, ADMIN_GET_USERS} from "./types";
import {ADMIN_GET_ROLES} from "./types";
import {ADMIN_GET_USER_DETAIL} from "./types";
import {ADMIN_GET_USER_DETAIL_FAIL} from "./types";
import {ADMIN_UPDATE_USER_DETAILS_FAIL} from "./types";
import {ADMIN_UPDATE_USER_DETAILS} from "./types";
import {ADMIN_UPDATE_USER_QUOTA} from "./types";
import {ADMIN_UPDATE_USER_QUOTA_FAIL} from "./types";

export const getUsers = endpoint => dispatch => {
    apiGetCall(endpoint).then(response => {
        dispatch({type: ADMIN_GET_USERS, payload: response.data});
    }).catch(error => {
        if (error.response) {
            dispatch({type: ADMIN_GET_USERS_FAIL, errorMessage: error.response.data});
        } else {
            dispatch({type: ADMIN_GET_USERS_FAIL, errorMessage: error.message});
        }
    })
};

export const getRoles = endpoint => dispatch => {
    apiGetCall(endpoint).then(response => {
        dispatch({type: ADMIN_GET_ROLES, payload: response.data});
    })
};
export const getUserDetail = (endpoint) => dispatch => {
    apiGetCall(endpoint).then(response => {
        dispatch({type: ADMIN_GET_USER_DETAIL, payload: response.data});
    }).catch(error => {
        if (error.response) {
            dispatch({type: ADMIN_GET_USER_DETAIL_FAIL, errorMessage: error.response.data});
        } else {
            dispatch({type: ADMIN_GET_USER_DETAIL_FAIL, errorMessage: error.message});
        }
    })
};

export const updateUserDetails = (endpoint, data, user) => dispatch => {
    const updatedUser = { username: user, password: data.password, email: data.email, role: data.role, enabled: data.enabled};

    apiPutCall(endpoint + user, updatedUser).then(() => {
        dispatch({type: ADMIN_UPDATE_USER_DETAILS, user: updatedUser});
    }).catch(error => {
        if (error.response) {
            dispatch({type: ADMIN_UPDATE_USER_DETAILS_FAIL, errorMessage: error.response.data});
        } else {
            dispatch({type: ADMIN_UPDATE_USER_DETAILS_FAIL, errorMessage: error.message});
        }
    })
};

export const updateUserQuota = (endpoint, data) => dispatch => {
    apiPutCall(endpoint, data).then(() => {
        dispatch({type: ADMIN_UPDATE_USER_QUOTA, quota: data});
    }).catch(error => {
        if (error.response) {
            dispatch({type: ADMIN_UPDATE_USER_QUOTA_FAIL, errorMessage: error.response.data});
        } else {
            dispatch({type: ADMIN_UPDATE_USER_QUOTA_FAIL, errorMessage: error.message});
        }
    })
};