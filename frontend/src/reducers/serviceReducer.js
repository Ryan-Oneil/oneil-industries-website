import { createSlice } from "@reduxjs/toolkit";
import { apiGetCall } from "../apis/api";
import { setError } from "./globalErrorReducer";
import { getApiError } from "../apis/ApiErrorHandler";

const slice = createSlice({
  name: "service",
  initialState: {
    activeDiscord: [],
  },
  reducers: {
    fetchedDiscordList(state, action) {
      state.activeDiscord = action.payload;
    },
  },
});
export default slice.reducer;
export const { fetchedDiscordList } = slice.actions;

export const getDiscordActiveList = () => (dispatch) => {
  apiGetCall("/services/public/discord")
    .then((response) => {
      dispatch(fetchedDiscordList(response.data));
    })
    .catch((error) => dispatch(setError(getApiError(error))));
};
