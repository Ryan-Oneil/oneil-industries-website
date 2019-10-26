import {MEDIA_FAILURE, MEDIA_REQUEST} from "../actions";

export default (state = [], action) => {

    switch (action.type) {
        case MEDIA_REQUEST: {
            return action.payload;
        }
        case MEDIA_FAILURE: {
            return action.message;
        }
        default: {
            return state;
        }
    }
};