import {
    UPDATE_USER_DETAILS_FAIL,
    UPDATE_USER_DETAILS_SENT,
    USER_PROFILE_GET,
    USER_PROFILE_GET_FAIL, USER_PROFILE_SERVICE_ADD_DISCORD, USER_PROFILE_SERVICE_ADD_FAIL, USER_PROFILE_SERVICE_ADD_TS,
    USER_PROFILE_SERVICE_DELETE_DISCORD,
    USER_PROFILE_SERVICE_DELETE_TS,
    USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS, USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS_FAIL
} from "../actions/types";

export default (state = {errorMessage: ""}, action) => {
    switch (action.type) {
        case USER_PROFILE_GET: {
            const {userDiscord, userTeamspeak, user} = action.payload;
            return {...state, userDiscord, userTeamspeak, user, errorMessage : ""};
        }
        case USER_PROFILE_GET_FAIL: {
            return {...state, errorMessage: action.errorMessage};
        }
        case UPDATE_USER_DETAILS_SENT: {
            return {...state, message: action.message, errorMessage: ""}
        }
        case UPDATE_USER_DETAILS_FAIL: {
            return {...state, errorMessage: action.errorMessage}
        }
        case USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS: {
            return {...state, teamspeakUsers: action.payload.teamspeakUsers, discordUsers: action.payload.discordUsers, errorMessage: ""}
        }
        case USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS_FAIL: {
            return {...state, errorMessage: action.errorMessage}
        }
        case USER_PROFILE_SERVICE_ADD_DISCORD: {
            return {...state, userDiscord: [...state.userDiscord, action.payload], discordUsers: state.discordUsers.filter(serviceClient => serviceClient.uuid !== action.payload.uuid)}
        }
        case USER_PROFILE_SERVICE_ADD_TS: {
            return {...state, userTeamspeak: [...state.userTeamspeak, action.payload], teamspeakUsers: state.teamspeakUsers.filter(serviceClient => serviceClient.uuid !== action.payload.uuid)}
        }
        case USER_PROFILE_SERVICE_ADD_FAIL: {
            return {...state, errorMessage: action.errorMessage}
        }
        case USER_PROFILE_SERVICE_DELETE_TS: {
            return {...state, userTeamspeak: state.userTeamspeak.filter(serviceClient => serviceClient.id !== action.serviceID)}
        }
        case USER_PROFILE_SERVICE_DELETE_DISCORD: {
            return {...state, userDiscord: state.userDiscord.filter(serviceClient => serviceClient.id !== action.serviceID)}
        }
        default: {
            return state;
        }
    }
};