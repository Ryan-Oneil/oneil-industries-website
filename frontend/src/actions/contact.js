import { apiPostCall } from "../apis/api";
import { CONTACT_FORM_SENT } from "./types";
import { SubmissionError } from "redux-form";

export const sendContactForm = (endpoint, form) => dispatch => {
  return apiPostCall(endpoint, form)
    .then(response => {
      dispatch({ type: CONTACT_FORM_SENT, payload: response.data });
    })
    .catch(error => {
      if (error.response) {
        throw new SubmissionError({ _error: error.response.data });
      } else {
        throw new SubmissionError({ _error: error.message });
      }
    });
};
