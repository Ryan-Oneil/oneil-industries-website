import {
  ALBUM_DELETE,
  ALBUM_EDIT_PUT,
  ALBUM_MEDIA_GET,
  ALBUM_REQUEST,
  MEDIA_DELETE_DONE,
  MEDIA_DELETE_FAIL,
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
    activeMedia: {},
    album: {}
  },
  action
) => {
  switch (action.type) {
    case MEDIA_REQUEST: {
      return { ...state, mediasList: action.payload };
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
          if (mediaAlbum.id === action.album.id) {
            mediaAlbum.name = action.album.name;
            mediaAlbum.showUnlistedImages = action.album.showUnlistedImages;
            return mediaAlbum;
          }
          return mediaAlbum;
        })
      };
    }
    case ALBUM_DELETE: {
      return {
        ...state,
        albums: state.albums.filter(
          mediaAlbum => mediaAlbum.id !== action.albumDeleteID
        )
      };
    }
    case MEDIA_RESET_MESSAGES: {
      return {
        ...state,
        mediaUpdateMessage: "",
        mediaPostMessage: "",
        mediaErrorMessage: ""
      };
    }
    case SET_ACTIVE_MEDIA_MODAL: {
      return {
        ...state,
        activeMedia: _.find(state.mediasList, media => {
          return media.id === action.mediaID;
        })
      };
    }
    case ALBUM_MEDIA_GET: {
      return {
        ...state,
        mediasList: action.medias,
        album: {
          id: action.payload.id,
          name: action.payload.name,
          creator: action.payload.creator
        }
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
