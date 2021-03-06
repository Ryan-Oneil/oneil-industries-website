import { createSlice } from "@reduxjs/toolkit";
import { normalize, schema } from "normalizr";
import {
  apiDeleteCall,
  apiGetCall,
  apiPostCall,
  apiPutCall,
} from "../apis/api";
import { setError } from "./globalErrorReducer";
import {
  ALBUM_CREATE,
  ALBUM_DELETE,
  ALBUM_UPDATE,
  USER_DELETE_MEDIAS_ENDPOINTS,
} from "../apis/endpoints";
import { increasedQuotaUsed } from "./userReducer";
import { getApiError } from "../apis/ApiErrorHandler";

const media = new schema.Entity("medias");
const album = new schema.Entity("albums", { medias: [media] });
const mediaList = new schema.Array(media);
const albumList = new schema.Array(album);

export const slice = createSlice({
  name: "media",
  initialState: {
    entities: {
      medias: {},
      albums: {},
      comments: {},
    },
    stats: {
      totalMedias: 0,
      recentMedias: [],
      totalAlbums: 0,
    },
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

      if (!data.entities.albums) {
        return;
      }
      state.entities.medias = Object.assign(
        {},
        state.entities.medias,
        data.entities.medias
      );
      state.entities.albums = data.entities.albums;
    },
    deletedMedias(state, action) {
      action.payload.forEach(
        (mediaId) => delete state.entities.medias[mediaId]
      );
    },
    updatedMedia(state, action) {
      const id = action.payload.id;

      state.entities.medias[id] = {
        ...state.entities.medias[id],
        ...action.payload,
      };
    },
    fetchedUserMediaStats(state, action) {
      state.stats = action.payload;
    },
    createdAlbum(state, action) {
      state.entities.albums[action.payload.id] = action.payload;
    },
    updatedAlbum(state, action) {
      state.entities.albums[action.payload.id] = {
        ...state.entities.albums[action.payload.id],
        ...action.payload.album,
      };
    },
    deletedAlbum(state, action) {
      delete state.entities.albums[action.payload];
    },
    updatedMediaLinkStatus(state, action) {
      action.payload.mediaIds.forEach(
        (mediaId) =>
          (state.entities.medias[mediaId].linkStatus =
            action.payload.linkStatus)
      );
    },
  },
});
export default slice.reducer;
export const {
  fetchedMedia,
  fetchedAlbums,
  updatedMedia,
  fetchedUserMediaStats,
  createdAlbum,
  updatedAlbum,
  deletedMedias,
  deletedAlbum,
  updatedMediaLinkStatus,
} = slice.actions;

export const fetchMedia = (endpoint, page, size) => (dispatch) => {
  const params = new URLSearchParams();
  params.append("page", page);
  params.append("size", size);

  return apiGetCall(endpoint, { params })
    .then((response) => dispatch(fetchedMedia(response.data)))
    .catch((error) => dispatch(setError(getApiError(error))));
};

export const fetchAlbums = (endpoint) => (dispatch) => {
  return apiGetCall(endpoint)
    .then((response) => dispatch(fetchedAlbums(response.data)))
    .catch((error) => dispatch(setError(getApiError(error))));
};

export const uploadMedia = (endpoint, data, file, uploadProgress) => (
  dispatch
) => {
  let postData = new FormData();
  postData.append("file[]", file, file.name);

  let options = {
    params: { albumId: data.albumId },
    onUploadProgress: (progressEvent) => uploadProgress(progressEvent),
  };

  if (data.albumName) {
    options.params.album = data.albumName;
    options.params.showUnlistedImages = data.showUnlisted;
  } else if (data.album) {
    options.params.album = data.album;
  }
  return apiPostCall(endpoint, postData, options).then((response) => {
    dispatch(increasedQuotaUsed(response.data[0].size));

    return dispatch(fetchedMedia({ medias: response.data }));
  });
};
export const deleteMedias = (mediaIDs) => (dispatch) => {
  return apiDeleteCall(USER_DELETE_MEDIAS_ENDPOINTS, {
    data: mediaIDs,
  })
    .then(() => {
      dispatch(deletedMedias(mediaIDs));
    })
    .catch((error) => dispatch(setError(getApiError(error))));
};

export const updateMedia = (data, mediaID) => (dispatch) => {
  const media = {
    id: mediaID,
    name: data.name,
    linkStatus: data.privacy,
  };

  return apiPutCall(`/gallery/media/update/${mediaID}`, data).then(() => {
    dispatch(updatedMedia(media));
  });
};

export const deleteAlbum = (albumID) => (dispatch) => {
  return apiDeleteCall(ALBUM_DELETE + albumID)
    .then(() => dispatch(deletedAlbum(albumID)))
    .catch((error) => dispatch(setError(getApiError(error))));
};
export const fetchAlbumWithImages = (endpoint) => {
  return apiGetCall(endpoint).then(({ data }) => data);
};

export const getUserMediaStats = (user) => (dispatch) => {
  return apiGetCall(`/gallery/user/${user}/stats`)
    .then((response) => dispatch(fetchedUserMediaStats(response.data)))
    .catch((error) => dispatch(setError(getApiError(error))));
};

export const postNewAlbum = (albumName) => (dispatch) => {
  const album = { name: albumName };

  return apiPostCall(ALBUM_CREATE, album)
    .then((response) => dispatch(createdAlbum(response.data)))
    .catch((error) => dispatch(setError(getApiError(error))));
};

export const updateAlbum = (album, albumId) => (dispatch) => {
  return apiPutCall(`${ALBUM_UPDATE}/${albumId}`, { name: album.name })
    .then(() => dispatch(updatedAlbum({ id: albumId, album })))
    .catch((error) => dispatch(setError(getApiError(error))));
};

export const addMediasToAlbum = (albumId, mediaIds) => (dispatch) => {
  return apiPutCall(
    `/gallery/myalbum/${albumId}/add`,
    mediaIds
  ).catch((error) => dispatch(setError(getApiError(error))));
};

export const updateMediasLinkStatus = (mediaIds, status) => (dispatch) => {
  return apiPutCall("/gallery/medias/linkstatus/update", {
    mediaIds,
    linkStatus: status,
  }).then(({ data }) => {
    dispatch(updatedMediaLinkStatus({ linkStatus: status, mediaIds }));

    return data;
  });
};
