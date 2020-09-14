// import {
//   ALBUM_DELETE,
//   ALBUM_EDIT_PUT,
//   ALBUM_MEDIA_GET,
//   ALBUM_REQUEST,
//   MEDIA_DELETE_DONE,
//   MEDIA_DELETE_FAIL,
//   MEDIA_POST_SENT,
//   MEDIA_REQUEST,
//   MEDIA_RESET_MESSAGES,
//   MEDIA_UPDATE_SENT,
//   SET_ACTIVE_MEDIA_MODAL
// } from "../actions/types";
//
// export default (
//   state = {
//     mediasList: [],
//     albums: [],
//     mediaUpdateMessage: "",
//     activeMedia: {},
//     album: {}
//   },
//   action
// ) => {
//   switch (action.type) {
//     case MEDIA_REQUEST: {
//       return { ...state, mediasList: action.payload };
//     }
//     case MEDIA_POST_SENT: {
//       return {
//         ...state,
//         mediaPostMessage: action.message,
//         mediaErrorMessage: ""
//       };
//     }
//     case MEDIA_DELETE_DONE: {
//       return {
//         ...state,
//         mediasList: state.mediasList.filter(
//           media => media.id !== action.mediaDeleteID
//         )
//       };
//     }
//     case MEDIA_DELETE_FAIL: {
//       return { ...state, deleteError: action.message };
//     }
//     case MEDIA_UPDATE_SENT: {
//       return {
//         ...state,
//         mediaUpdateMessage: action.message,
//         mediasList: state.mediasList.map(media =>
//           changeMedia(media, action.media)
//         ),
//         activeMedia: changeMedia(state.activeMedia, action.media)
//       };
//     }
//     case ALBUM_REQUEST: {
//       return { ...state, albums: action.payload };
//     }
//     case ALBUM_EDIT_PUT: {
//       return {
//         ...state,
//         albums: state.albums.map(mediaAlbum => {
//           if (mediaAlbum.id === action.album.id) {
//             mediaAlbum.name = action.album.name;
//             mediaAlbum.showUnlistedImages = action.album.showUnlistedImages;
//             return mediaAlbum;
//           }
//           return mediaAlbum;
//         })
//       };
//     }
//     case ALBUM_DELETE: {
//       return {
//         ...state,
//         albums: state.albums.filter(
//           mediaAlbum => mediaAlbum.id !== action.albumDeleteID
//         )
//       };
//     }
//     case MEDIA_RESET_MESSAGES: {
//       return {
//         ...state,
//         mediaUpdateMessage: "",
//         mediaPostMessage: "",
//         mediaErrorMessage: ""
//       };
//     }
//     case SET_ACTIVE_MEDIA_MODAL: {
//       return {
//         ...state,
//         activeMedia: _.find(state.mediasList, media => {
//           return media.id === action.mediaID;
//         })
//       };
//     }
//     case ALBUM_MEDIA_GET: {
//       return {
//         ...state,
//         mediasList: action.medias,
//         album: {
//           id: action.payload.id,
//           name: action.payload.name,
//           creator: action.payload.creator
//         }
//       };
//     }
//     default: {
//       return state;
//     }
//   }
// };
//
// const changeMedia = (media, actionMedia) => {
//   if (media.id === actionMedia.id) {
//     return {
//       ...media,
//       name: actionMedia.name,
//       linkStatus: actionMedia.linkStatus
//     };
//   }
//   return media;
// };
import { createSlice } from "@reduxjs/toolkit";
import { normalize, schema } from "normalizr";
import {
  ALBUM_DELETE,
  ALBUM_EDIT_PUT,
  ALBUM_MEDIA_GET,
  MEDIA_UPDATE_SENT
} from "../actions/types";
import {
  apiDeleteCall,
  apiGetCall,
  apiPostCall,
  apiPutCall
} from "../apis/api";
import { getApiError } from "../helpers";
import { setError } from "./globalErrorReducer";

export const fetchImages = (endpoint, page, size) => dispatch => {
  const params = new URLSearchParams();
  params.append("page", page);
  params.append("size", size);

  return apiGetCall(endpoint, { params })
    .then(response => dispatch(fetchedMedia(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const fetchAlbums = endpoint => {
  return apiGetCall(endpoint).then(response => response.data);
};

export const updateAlbum = (endpoint, id, payload) => dispatch => {
  return apiPutCall(endpoint + id, payload).then(() => {
    dispatch({
      type: ALBUM_EDIT_PUT,
      album: {
        id,
        name: payload.newAlbumName,
        showUnlistedImages: payload.showUnlistedImages
      }
    });
  });
};

export const uploadMedia = (endpoint, data, files) => {
  let postData = new FormData();

  files.forEach(media => postData.append("file[]", media, media.name));

  let options = {
    params: { privacy: data.linkStatus }
  };

  if (data.albumName) {
    options.params.album = data.albumName;
    options.params.showUnlistedImages = data.showUnlisted;
  } else if (data.album) {
    options.params.album = data.album;
  }

  return apiPostCall(endpoint, postData, options).then(
    response => response.data
  );
};

export const deleteMedia = (endpoint, mediaID) => dispatch => {
  apiDeleteCall(endpoint)
    .then(() => {
      dispatch(deletedMedia(mediaID));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const updateMedia = (data, mediaID) => dispatch => {
  const updatedMedia = {
    id: mediaID,
    name: data.name,
    linkStatus: data.privacy
  };

  return apiPutCall(`/gallery/media/update/${mediaID}`, data).then(response => {
    dispatch({
      type: MEDIA_UPDATE_SENT,
      media: updatedMedia,
      message: response.data
    });
  });
};

export const deleteAlbum = (endpoint, albumID) => dispatch => {
  apiDeleteCall(endpoint + albumID)
    .then(() => {
      dispatch({
        type: ALBUM_DELETE,
        albumDeleteID: albumID
      });
    })
    .catch(error => dispatch(setError(getApiError(error))));
};
export const fetchAlbumWithImages = endpoint => dispatch => {
  apiGetCall(endpoint)
    .then(response => {
      dispatch({
        type: ALBUM_MEDIA_GET,
        payload: response.data,
        medias: response.data.medias
      });
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

const media = new schema.Entity("medias");
const album = new schema.Entity("albums", { medias: [media] });
const mediaList = new schema.Array(media);
const albumList = new schema.Array(album);

export const slice = createSlice({
  name: "media",
  initialState: {
    entities: {
      medias: {},
      albums: {}
    }
  },
  reducers: {
    fetchedMedia(state, action) {
      const data = normalize(action.payload.medias, mediaList);

      state.entities.medias = Object.assign(
        {},
        state.entities.medias,
        data.entities.medias
      );
    },
    fetchedAlbums(state, action) {
      const data = normalize(action.payload, albumList);

      state.entities.albums = data.entities.albums;
    },
    deletedMedia(state, action) {
      delete state.entities.medias[action.payload];
    }
  }
});
export default slice.reducer;
export const { fetchedMedia, fetchedAlbums, deletedMedia } = slice.actions;
