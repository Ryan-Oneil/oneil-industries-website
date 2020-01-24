import {
  UPDATE_USER_DETAILS_SENT,
  USER_PROFILE_GENERATE_API_TOKEN,
  USER_PROFILE_GENERATE_SHAREX_CONFIG,
  USER_PROFILE_GET,
  USER_PROFILE_GET_API_TOKENS,
  USER_PROFILE_GET_STORAGE_QUOTA,
  USER_PROFILE_SERVICE_ADD_DISCORD,
  USER_PROFILE_SERVICE_ADD_TS,
  USER_PROFILE_SERVICE_DELETE_DISCORD,
  USER_PROFILE_SERVICE_DELETE_TS,
  USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS
} from "../actions/types";

export default (
  state = {
    errorMessage: "",
    teamspeakUsers: [],
    discordUsers: [],
    userDiscord: [],
    userTeamspeak: [],
    user: "",
    storageQuota: [],
    canUpload: true
  },
  action
) => {
  switch (action.type) {
    case USER_PROFILE_GET: {
      const { userDiscord, userTeamspeak, user, storageQuota } = action.payload;
      return {
        ...state,
        userDiscord,
        userTeamspeak,
        user,
        storageQuota,
        errorMessage: ""
      };
    }
    case UPDATE_USER_DETAILS_SENT: {
      return {
        ...state,
        user: { ...state.user, email: action.updatedDetails.email }
      };
    }
    case USER_PROFILE_SERVICE_GET_UNREGISTERED_ACCOUNTS: {
      return {
        ...state,
        teamspeakUsers: action.payload.teamspeakUsers,
        discordUsers: action.payload.discordUsers,
        errorMessage: ""
      };
    }
    case USER_PROFILE_SERVICE_ADD_DISCORD: {
      return {
        ...state,
        userDiscord: [...state.userDiscord, action.payload],
        discordUsers: state.discordUsers.filter(
          serviceClient => serviceClient.uuid !== action.payload.uuid
        )
      };
    }
    case USER_PROFILE_SERVICE_ADD_TS: {
      return {
        ...state,
        userTeamspeak: [...state.userTeamspeak, action.payload],
        teamspeakUsers: state.teamspeakUsers.filter(
          serviceClient => serviceClient.uuid !== action.payload.uuid
        )
      };
    }
    case USER_PROFILE_SERVICE_DELETE_TS: {
      return {
        ...state,
        userTeamspeak: state.userTeamspeak.filter(
          serviceClient => serviceClient.id !== action.serviceID
        )
      };
    }
    case USER_PROFILE_SERVICE_DELETE_DISCORD: {
      return {
        ...state,
        userDiscord: state.userDiscord.filter(
          serviceClient => serviceClient.id !== action.serviceID
        )
      };
    }
    case USER_PROFILE_GET_API_TOKENS: {
      return { ...state, apiToken: action.payload };
    }
    case USER_PROFILE_GENERATE_SHAREX_CONFIG: {
      return { ...state, shareXConfig: action.payload, shareXError: "" };
    }
    case USER_PROFILE_GENERATE_API_TOKEN: {
      return { ...state, apiToken: action.payload };
    }
    case USER_PROFILE_GET_STORAGE_QUOTA: {
      return { ...state, storageQuota: action.payload };
    }
    default: {
      return state;
    }
  }
};
