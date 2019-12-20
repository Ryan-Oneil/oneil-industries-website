import { CONTACT_FORM_FAIL, CONTACT_FORM_SENT } from "../actions/types";

export default (state = [], action) => {
  switch (action.type) {
    case CONTACT_FORM_SENT: {
      return { ...state, contactFormSent: true, message: action.payload };
    }
    case CONTACT_FORM_FAIL: {
      return { ...state, errorMessage: action.message };
    }
    default: {
      return state;
    }
  }
};
