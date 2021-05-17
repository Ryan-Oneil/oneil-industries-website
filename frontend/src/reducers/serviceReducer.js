import { createSlice } from "@reduxjs/toolkit";
import { apiGetCall } from "../apis/api";
import { setError } from "./globalErrorReducer";
import { getApiError } from "../apis/ApiErrorHandler";

const slice = createSlice({
  name: "service",
  initialState: {
    activeTSList: [],
    activeDiscord: []
  },
  reducers: {
    fetchedTeamspeakList(state, action) {
      state.activeTSList = action.payload;
    },
    fetchedDiscordList(state, action) {
      state.activeDiscord = action.payload;
    }
  }
});
export default slice.reducer;
export const { fetchedTeamspeakList, fetchedDiscordList } = slice.actions;

export const getTeamspeakActiveList = () => dispatch => {
  apiGetCall("/services/public/teamspeak")
    .then(response => {
      dispatch(fetchedTeamspeakList(response.data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getDiscordActiveList = () => dispatch => {
  apiGetCall("/services/public/discord")
    .then(response => {
      dispatch(fetchedDiscordList(response.data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};
