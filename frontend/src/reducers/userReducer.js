import { createSlice } from "@reduxjs/toolkit";
import { apiGetCall, apiPutCall } from "../apis/api";
import { setError } from "./globalErrorReducer";
import { getApiError } from "../apis/ApiErrorHandler";

export const slice = createSlice({
  name: "users",
  initialState: {
    details: { name: "", email: "", role: "" },
    //Default quota given is 25gb
    storageQuota: { used: 0, max: 25 },
    apiToken: "",
    shareXConfig: ""
  },
  reducers: {
    updateMyEmail(state, action) {
      state.details.email = action.payload.email;
    },
    getDetails(state, action) {
      state.details = action.payload;
      state.storageQuota = action.payload.quota;
    },
    getQuota(state, action) {
      state.storageQuota = action.payload;
    },
    getToken(state, action) {
      state.apiToken = action.payload;
    },
    getShareXConfig(state, action) {
      state.shareXConfig = action.payload;
    },
    increasedQuotaUsed(state, action) {
      state.storageQuota.used += action.payload;
    },
    decreaseQuotaUsed(state, action) {
      state.storageQuota.used -= action.payload;
    }
  }
});
export default slice.reducer;
export const {
  updateMyEmail,
  getDetails,
  getQuota,
  getToken,
  getShareXConfig,
  increasedQuotaUsed,
  decreaseQuotaUsed
} = slice.actions;

export const getQuotaStats = username => dispatch => {
  return apiGetCall(`/user/${username}/quota`)
    .then(response => dispatch(getQuota(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getUserDetails = username => dispatch => {
  return apiGetCall(`/user/${username}/details`)
    .then(response => dispatch(getDetails(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const updateEmail = (username, email) => dispatch => {
  return apiPutCall(`/user/${username}/details/update`, email).then(() =>
    dispatch(updateMyEmail(email))
  );
};

export const fetchAPIToken = endpoint => dispatch => {
  return apiGetCall(endpoint)
    .then(({ data }) => {
      dispatch(getToken(data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const generateShareXConfig = endpoint => dispatch => {
  return apiGetCall(endpoint)
    .then(({ data }) => {
      dispatch(getShareXConfig(data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};
