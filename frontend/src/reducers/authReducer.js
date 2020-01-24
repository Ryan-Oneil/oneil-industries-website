import {
  LOGIN_REQUEST,
  LOGIN_SUCCESS,
  LOGOUT_SUCCESS,
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  RESET_PASSWORD_SENT,
  NEW_PASSWORD_SENT
} from "../actions/types";
import { decodeJWT, isTokenExpired } from "../actions";

export default function auth(
  state = {
    isAuthenticated: isAuth(),
    user: decodeJWT("refreshToken").user,
    role: decodeJWT("authToken").role
  },
  action
) {
  switch (action.type) {
    case LOGIN_REQUEST:
      return Object.assign({}, state, {
        isAuthenticated: false,
        user: action.creds.username
      });
    case LOGIN_SUCCESS:
      return Object.assign({}, state, {
        isAuthenticated: true,
        errorMessage: ""
      });
    case LOGOUT_SUCCESS:
      return Object.assign({}, state, {
        isAuthenticated: false,
        user: ""
      });
    case REGISTER_SUCCESS: {
      return {
        ...state,
        isRegistered: true,
        message: action.message,
        errorMessage: ""
      };
    }
    case REGISTER_FAIL: {
      return { ...state, errorMessage: action.errorMessage, message: "" };
    }
    case RESET_PASSWORD_SENT: {
      return {
        ...state,
        hasSentResetEmail: true,
        message: action.message,
        errorMessage: ""
      };
    }
    case NEW_PASSWORD_SENT: {
      return {
        ...state,
        message: action.message,
        errorMessage: "",
        passwordReset: true
      };
    }
    default:
      return state;
  }
}

const isAuth = () => {
  const token = decodeJWT("refreshToken");

  if (!token) {
    return false;
  }
  return !isTokenExpired(token);
};
