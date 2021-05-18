import { createSlice } from "@reduxjs/toolkit";
import { apiPostCall, BASE_URL, getRefreshToken } from "../apis/api";

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
      state.user = { name: action.payload };
      state.isAuthenticated = true;
    },
    logout(state) {
      state.isAuthenticated = false;
      state.user = {};
      state.role = "";
    },
    setUserRole(state, action) {
      state.role = action.payload;
    }
  }
});
export default slice.reducer;
export const { loginSuccess, logout, setUserRole } = slice.actions;

export const getRefreshTokenWithRole = () => dispatch => {
  const refreshToken = localStorage.getItem("refreshToken");

  if (refreshToken) {
    return getRefreshToken(refreshToken).then(() =>
      dispatch(setUserRole(decodeJWT("authToken").role))
    );
  }
};

export const loginUser = creds => dispatch => {
  return apiPostCall(BASE_URL + "/login", creds).then(response => {
    const token = response.headers["authorization"];
    localStorage.setItem("refreshToken", token);

    return dispatch(getRefreshTokenWithRole()).then(() =>
      dispatch(loginSuccess(decodeJWT("refreshToken").user))
    );
  });
};

export const registerUser = creds => {
  return apiPostCall(BASE_URL + "/auth/register", creds);
};

export const logoutUser = () => {
  return dispatch => {
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("authToken");
    dispatch(logout());
  };
};

export const resetPassword = value => {
  return apiPostCall(BASE_URL + "/auth/forgotPassword/" + value.email);
};

export const changePassword = (token, value) => {
  return apiPostCall(BASE_URL + "/auth/newPassword/" + token, value.password);
};
