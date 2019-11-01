import {
    SERVICES_DISCORD_GET_ACTIVE_LIST_FAIL,
    SERVICES_DISCORD_GET_ACTIVE_LIST_SENT,
    SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_FAIL,
    SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_SENT
} from '../actions/types';

export default (state = [], action) => {

    switch (action.type) {
        case SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_SENT: {
            return {...state, activeTSList: action.payload};
        }
        case SERVICES_TEAMSPEAK_GET_ACTIVE_LIST_FAIL: {
            return {...state, errorMessage: action.message};
        }
        case SERVICES_DISCORD_GET_ACTIVE_LIST_SENT: {
            return {...state, activeDiscord: action.payload};
        }
        case SERVICES_DISCORD_GET_ACTIVE_LIST_FAIL: {
            return {...state, errorMessage: action.message};
        }
        default: {
            return state;
        }
    }
};