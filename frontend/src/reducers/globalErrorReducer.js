import { CLEAR_ERROR, DISABLE_ERRORS, SET_ERROR } from "../actions/types";

export default (
  state = {
    error: "",
    disablePopup: false
  },
  action
) => {
  switch (action.type) {
    case SET_ERROR: {
      //Prevents new errors from overlapping an existing error popup and checks if the user disabled popups
      if (state.error || state.disablePopup) {
        return { ...state };
      }
      return { ...state, error: action.error };
    }
    case CLEAR_ERROR: {
      return { ...state, error: "" };
    }
    case DISABLE_ERRORS: {
      return { ...state, error: "", disablePopup: true };
    }
    default: {
      return state;
    }
  }
};
