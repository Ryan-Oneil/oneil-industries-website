import {apiPostCall} from "../apis/api";
import {CONTACT_FORM_FAIL, CONTACT_FORM_SENT} from "./types";

export const sendContactForm = (endpoint, form) => dispatch => {
    return new Promise((resolve, reject) => {
        apiPostCall(endpoint, form).then(response => {
            dispatch({type: CONTACT_FORM_SENT, payload: response.data});
            resolve(response);
        }).catch(error => {
            if (error.response) {
                dispatch({type: CONTACT_FORM_FAIL, errorMessage: error.response.data});
            } else {
                dispatch({type: CONTACT_FORM_FAIL, errorMessage: error.message});
            }
            reject(error);
            }
        );
    })
};