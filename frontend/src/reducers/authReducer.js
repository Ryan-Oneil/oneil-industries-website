import {
    LOGIN_REQUEST,
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    LOGOUT_SUCCESS,
    REGISTER_SUCCESS,
    REGISTER_FAIL,
    RESET_PASSWORD_SENT,
    RESET_PASSWORD_FAIL
} from '../actions/types'

export default function auth(state = {
    isAuthenticated: localStorage.getItem('token') ? true : false,
    user: getUserNameFromJWT()
}, action) {
    switch (action.type) {
        case LOGIN_REQUEST:
            return Object.assign({}, state, {
                isAuthenticated: false,
                user: action.creds.username
            });
        case LOGIN_SUCCESS:
            return Object.assign({}, state, {
                isAuthenticated: true,
                errorMessage: ''
            });
        case LOGIN_FAILURE:
            return Object.assign({}, state, {
                isAuthenticated: false,
                errorMessage: action.message
            });
        case LOGOUT_SUCCESS:
            return Object.assign({}, state, {
                isAuthenticated: false,
                user: ""
            });
        case REGISTER_SUCCESS: {
            return {...state, isRegistered: true, message: action.message};
        }
        case REGISTER_FAIL: {
            return {...state, errorMessage: action.errorMessage}
        }
        case RESET_PASSWORD_SENT: {
            return {...state, hasSentResetEmail: true, message: action.message, errorMessage: null}
        }
        case RESET_PASSWORD_FAIL: {
            return {...state, errorMessage: action.errorMessage}
        }
        default:
            return state
    }
}

const getUserNameFromJWT = () => {
    const token = localStorage.getItem('token');

    if (!token) {
        return "";
    }
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace('-', '+').replace('_', '/');
    return JSON.parse(window.atob(base64)).user;
};