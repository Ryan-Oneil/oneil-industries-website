import { createSlice } from "@reduxjs/toolkit";
import {
  apiDeleteCall,
  apiGetCall,
  apiPostCall,
  apiPutCall
} from "../apis/api";

import { setError } from "./globalErrorReducer";
import { getApiError, getFilterSort } from "../helpers";
import { normalize, schema } from "normalizr";

export const deleteLink = linkID => dispatch => {
  apiDeleteCall(`/delete/${linkID}`)
    .then(() => {
      dispatch(removeLink({ linkID: linkID }));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getLinkDetails = linkID => dispatch => {
  return apiGetCall(`/info/${linkID}`)
    .then(response => {
      dispatch(addLinkInfo(response.data));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const deleteFile = (fileID, linkID) => dispatch => {
  apiDeleteCall(`/file/delete/${fileID}`)
    .then(() => {
      dispatch(removeFile({ fileID, linkID }));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const addFilesToLink = (files, linkID) => dispatch => {
  return uploadFiles(`/link/add/${linkID}`, files)
    .then(response => {
      dispatch(addFiles({ files: response.data, linkID: linkID }));
    })
    .catch(error => dispatch(setError(getApiError(error))));
};

export const uploadFiles = (endpoint, files, params = {}) => {
  let postData = new FormData();

  files.forEach(file => postData.append("file[]", file, file.name));

  let options = {
    params: params
  };
  return apiPostCall(endpoint, postData, options);
};

export const getUserLinkStats = user => dispatch => {
  return apiGetCall(`/user/${user}/links/stats`)
    .then(response => dispatch(getFileStats(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const editLink = (linkID, link) => dispatch => {
  return apiPutCall(`/link/edit/${linkID}`, link).then(() =>
    dispatch(updateLinkDetails({ link: link, linkID: linkID }))
  );
};

export const getAllLinksPageable = (
  page,
  size,
  sorter = { order: "", field: "" }
) => dispatch => {
  return getLinksPageable("/admin/links", page, size, getFilterSort(sorter))
    .then(response => dispatch(addLinks(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

export const getLinksPageable = (endpoint, page, size, sortAttribute) => {
  const params = new URLSearchParams();
  params.append("page", page);
  params.append("size", size);
  if (sortAttribute) params.append("sort", sortAttribute);

  return apiGetCall(endpoint, { params });
};

const baseGetUserLinks = (
  username,
  page,
  size,
  sorter = { order: "", field: "" }
) => {
  return getLinksPageable(
    `/user/${username}/links`,
    page,
    size,
    getFilterSort(sorter)
  );
};

export const getUserLinks = (username, page, size, sorter) => dispatch => {
  return baseGetUserLinks(username, page, size, sorter)
    .then(response => dispatch(addLinks(response.data)))
    .catch(error => dispatch(setError(getApiError(error))));
};

const file = new schema.Entity("files");
const link = new schema.Entity("links", { files: [file] });
const linkList = new schema.Array(link);

export const slice = createSlice({
  name: "fileSharer",
  initialState: {
    entities: { files: {}, links: {} },
    stats: {
      totalViews: 0,
      totalLinks: 0,
      mostViewedLinks: [],
      recentLinks: []
    },
    linkUpload: {
      files: [],
      size: 0,
      reachedLimit: false
    }
  },
  reducers: {
    removeLink(state, action) {
      delete state.entities.links[action.payload.linkID];
    },
    addLinkInfo(state, action) {
      const normalizedData = normalize(action.payload, link);

      state.entities.files = Object.assign(
        {},
        state.entities.files,
        normalizedData.entities.files
      );

      state.entities.links = Object.assign(
        {},
        state.entities.links,
        normalizedData.entities.links
      );
    },
    removeFile(state, action) {
      const { files } = state.entities.links[action.payload.linkID];
      state.entities.links[action.payload.linkID].files = files.filter(
        fileID => fileID !== action.payload.fileID
      );
      delete state.entities.files[action.payload.fileID];
    },
    addFiles(state, action) {
      let files = {};
      let fileIds = [];

      action.payload.files.forEach(file => {
        files[file.id] = file;
        fileIds.push(file.id);
      });

      state.entities.files = Object.assign({}, state.entities.files, files);
      state.entities.links[action.payload.linkID].files = [
        ...state.entities.links[action.payload.linkID].files,
        ...fileIds
      ];
    },
    getFileStats(state, action) {
      //Change it so the files get added to entities
      //Store Ids only in stats array
      state.stats = action.payload;
    },
    updateLinkDetails(state, action) {
      state.entities.links[action.payload.linkID] = {
        ...state.entities.links[action.payload.linkID],
        ...action.payload.link
      };
    },
    addLinks(state, action) {
      const data = normalize(action.payload.links, linkList);
      state.stats.totalLinks = action.payload.total;

      state.entities.links = Object.assign(
        {},
        state.entities.links,
        data.entities.links
      );
    }
  }
});
export default slice.reducer;
export const {
  removeLink,
  addLinkInfo,
  removeFile,
  addFiles,
  getFileStats,
  updateLinkDetails,
  addLinks
} = slice.actions;
