import {apiGetCall} from "../apis/api";
import {
    SERVICES_DISCORD_GET_ACTIVE_LIST_FAIL, SERVICES_DISCORD_GET_ACTIVE_LIST_SENT,
    SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_FAIL,
    SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_SENT
} from "./types";

export const getTeamspeakActiveList = (endpoint) => dispatch => {
        apiGetCall(endpoint).then(response => {
            dispatch({type: SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_SENT, payload: response.data});
        }).catch(error => {
                if (error.response) {
                    dispatch({type: SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_FAIL, errorMessage: error.response.data});
                } else {
                    dispatch({type: SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_FAIL, errorMessage: error.message});
                }
            }
        );
};

export const getDiscordActiveList = (endpoint) => dispatch => {
    apiGetCall(endpoint).then(response => {
        dispatch({type: SERVICES_DISCORD_GET_ACTIVE_LIST_SENT, payload: response.data});
    }).catch(error => {
            if (error.response) {
                dispatch({type: SERVICES_DISCORD_GET_ACTIVE_LIST_FAIL, errorMessage: error.response.data});
            } else {
                dispatch({type: SERVICES_DISCORD_GET_ACTIVE_LIST_FAIL, errorMessage: error.message});
            }
        }
    );
};