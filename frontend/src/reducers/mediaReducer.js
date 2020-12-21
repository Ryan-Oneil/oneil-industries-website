import { createSlice } from "@reduxjs/toolkit";
import { normalize, schema } from "normalizr";
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
  const media = {
    id: mediaID,
    name: data.name,
    linkStatus: data.privacy
  };

  return apiPutCall(`/gallery/media/update/${mediaID}`, data).then(() => {
    dispatch(updatedMedia(media));
  });
};

export const deleteAlbum = (endpoint, albumID) => dispatch => {
  apiDeleteCall(endpoint + albumID).catch(error =>
    dispatch(setError(getApiError(error)))
  );
};
export const fetchAlbumWithImages = endpoint => {
  return apiGetCall(endpoint).then(({ data }) => data);
};

export const getUserMediaStats = user => dispatch => {
  return apiGetCall(`/gallery/user/${user}/stats`)
    .then(response => dispatch(fetchedUserMediaStats(response.data)))
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
    },
    stats: {
      totalMedias: 0,
      recentMedias: [],
      totalAlbums: 0
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
    },
    updatedMedia(state, action) {
      const id = action.payload.id;

      state.entities.medias[id] = {
        ...state.entities.medias[id],
        ...action.payload
      };
    },
    fetchedUserMediaStats(state, action) {
      state.stats = action.payload;
    }
  }
});
export default slice.reducer;
export const {
  fetchedMedia,
  fetchedAlbums,
  deletedMedia,
  updatedMedia,
  fetchedUserMediaStats
} = slice.actions;
