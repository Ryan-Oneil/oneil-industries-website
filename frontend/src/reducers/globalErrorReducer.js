import { createSlice } from "@reduxjs/toolkit";

export const slice = createSlice({
  name: "globalErrors",
  initialState: {
    error: "",
    disablePopup: false
  },
  reducers: {
    setError(state, action) {
      if (!state.error && !state.disablePopup) {
        state.error = action.payload;
      }
    },
    clearError(state) {
      state.error = "";
    },
    disableError(state) {
      state.disablePopup = true;
    }
  }
});
export default slice.reducer;
export const { setError, clearError, disableError } = slice.actions;
