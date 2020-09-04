import { createSlice } from "@reduxjs/toolkit";
import { apiPostCall, BASE_URL } from "../apis/api";

export const loginUser = creds => dispatch => {
  return apiPostCall(BASE_URL + "/login", creds).then(response => {
    const token = response.headers["authorization"];
    localStorage.setItem("refreshToken", token);
    dispatch(loginSuccess(creds));
  });
};

export const registerUser = creds => {
  return apiPostCall(BASE_URL + "/user/register", creds);
};

export const logoutUser = () => {
  return dispatch => {
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("authToken");
    dispatch(logout());
  };
};

export const resetPassword = value => {
  return apiPostCall(BASE_URL + "/user/forgotPassword/" + value.email);
};

export const changePassword = (token, value) => {
  return apiPostCall(BASE_URL + "/user/newPassword/" + token, value);
};

export const isTokenExpired = token => {
  return Date.now() > token.exp * 1000;
};

export const decodeJWT = tokenType => {
  const token = localStorage.getItem(tokenType);

  if (!token) {
    return "";
  }
  const base64Url = token.split(".")[1];
  const base64 = base64Url.replace("-", "+").replace("_", "/");

  return JSON.parse(window.atob(base64));
};

const isAuth = () => {
  const token = decodeJWT("refreshToken");

  if (!token) {
    return false;
  }
  return !isTokenExpired(token);
};

export const slice = createSlice({
  name: "auth",
  initialState: {
    isAuthenticated: isAuth(),
    user: { name: decodeJWT("refreshToken").user, avatar: "" },
    role: decodeJWT("authToken").role
  },
  reducers: {
    loginSuccess(state, action) {
      state.user = { name: action.payload.username };
      state.isAuthenticated = true;
    },
    logout(state) {
      state.isAuthenticated = false;
      state.user = {};
    }
  }
});
export default slice.reducer;
export const { loginSuccess, logout } = slice.actions;
