import {
  ALBUM_EDIT_PUT,
  ALBUM_FAILURE,
  ALBUM_REQUEST,
  MEDIA_DELETE_DONE,
  MEDIA_DELETE_FAIL,
  MEDIA_FAILURE,
  MEDIA_POST_SENT,
  MEDIA_REQUEST,
  MEDIA_RESET_MESSAGES,
  MEDIA_UPDATE_SENT,
  SET_ACTIVE_MEDIA_MODAL
} from "../actions/types";
import _ from "lodash";

export default (
  state = {
    mediasList: [],
    albums: [],
    mediaUpdateMessage: "",
    activeMedia: {}
  },
  action
) => {
  switch (action.type) {
    case MEDIA_REQUEST: {
      return { ...state, mediasList: action.payload };
    }
    case MEDIA_FAILURE: {
      return action.message;
    }
    case MEDIA_POST_SENT: {
      return {
        ...state,
        mediaPostMessage: action.message,
        mediaErrorMessage: ""
      };
    }
    case MEDIA_DELETE_DONE: {
      return {
        ...state,
        mediasList: state.mediasList.filter(
          media => media.id !== action.mediaDeleteID
        )
      };
    }
    case MEDIA_DELETE_FAIL: {
      return { ...state, deleteError: action.message };
    }
    case MEDIA_UPDATE_SENT: {
      return {
        ...state,
        mediaUpdateMessage: action.message,
        mediasList: state.mediasList.map(media =>
          changeMedia(media, action.media)
        ),
        activeMedia: changeMedia(state.activeMedia, action.media)
      };
    }
    case ALBUM_REQUEST: {
      return { ...state, albums: action.payload };
    }
    case ALBUM_EDIT_PUT: {
      return {
        ...state,
        albums: state.albums.map(mediaAlbum => {
          if (mediaAlbum.album.id === action.album.id) {
            mediaAlbum.album.name = action.album.name;
            mediaAlbum.album.showUnlistedImages =
              action.album.showUnlistedImages;
            return mediaAlbum;
          }
          return mediaAlbum;
        })
      };
    }
    case ALBUM_FAILURE: {
      return action.message;
    }
    case MEDIA_RESET_MESSAGES: {
      return { ...state, mediaUpdateMessage: "" };
    }
    case SET_ACTIVE_MEDIA_MODAL: {
      return {
        ...state,
        activeMedia: _.find(state.mediasList, media => {
          return media.id === action.mediaID;
        })
      };
    }
    default: {
      return state;
    }
  }
};

const changeMedia = (media, actionMedia) => {
  if (media.id === actionMedia.id) {
    return {
      ...media,
      name: actionMedia.name,
      linkStatus: actionMedia.linkStatus
    };
  }
  return media;
};
