import {
    LOGIN_REQUEST,
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    LOGOUT_SUCCESS,
    REGISTER_SUCCESS,
    REGISTER_FAIL,
    RESET_PASSWORD_SENT,
    RESET_PASSWORD_FAIL, NEW_PASSWORD_SENT, NEW_PASSWORD_FAIL
} from '../actions/types'

export default function auth(state = {
    isAuthenticated: isAuth(),
    user: decodeJWT('authToken').user,
    role: decodeJWT('authToken').role
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
            return {...state, isRegistered: true, message: action.message, errorMessage: ""};
        }
        case REGISTER_FAIL: {
            return {...state, errorMessage: action.errorMessage, message: ""}
        }
        case RESET_PASSWORD_SENT: {
            return {...state, hasSentResetEmail: true, message: action.message, errorMessage: ""}
        }
        case RESET_PASSWORD_FAIL: {
            return {...state, errorMessage: action.errorMessage}
        }
        case NEW_PASSWORD_SENT: {
            return {...state, message: action.message, errorMessage: "", passwordReset: true}
        }
        case NEW_PASSWORD_FAIL: {
            return {...state, message: "", errorMessage: action.errorMessage}
        }
        default:
            return state
    }
}

const decodeJWT = (tokenType) => {
    const token = localStorage.getItem(tokenType);

    if (!token) {
        return "";
    }

    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace('-', '+').replace('_', '/');

    return JSON.parse(window.atob(base64))
};

const isAuth = () => {
    const token = decodeJWT('refreshToken');

    if (!token) {
        return false;
    }

    if (Date.now() > token.exp * 1000) {
        localStorage.removeItem('refreshToken');
        return false;
    }
    return true;
};