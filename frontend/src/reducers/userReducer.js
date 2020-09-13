import { createSlice } from "@reduxjs/toolkit";
import { apiGetCall, apiPutCall } from "../apis/api";
import { getApiError } from "../helpers";
import { setError } from "./globalErrorReducer";

export const getQuotaStats = username => dispatch => {
  return apiGetCall(`/user/${username}/quota`)
    .then(response => dispatch(getQuota(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getUserDetails = username => dispatch => {
  apiGetCall(`/user/${username}/details`)
    .then(response => dispatch(getDetails(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const updateEmail = (username, email) => dispatch => {
  return apiPutCall(`/user/${username}/details/update`, email).then(() =>
    dispatch(updateMyEmail(email))
  );
};

export const slice = createSlice({
  name: "users",
  initialState: {
    details: { name: "", email: "", role: "" },
    //Default quota given is 25gb
    storageQuota: { used: 0, max: 25 }
  },
  reducers: {
    updateMyEmail(state, action) {
      state.details.email = action.payload.email;
    },
    getDetails(state, action) {
      state.details = action.payload;
    },
    getQuota(state, action) {
      state.storageQuota = action.payload;
    }
  }
});
export default slice.reducer;
export const { updateMyEmail, getDetails, getQuota } = slice.actions;
