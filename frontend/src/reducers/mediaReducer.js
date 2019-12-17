import {
    ALBUM_EDIT_PUT,
    ALBUM_FAILURE,
    ALBUM_REQUEST, MEDIA_DELETE_DONE, MEDIA_DELETE_FAIL,
    MEDIA_FAILURE,
    MEDIA_POST_FAIL,
    MEDIA_POST_SENT,
    MEDIA_POST_SUCCESS,
    MEDIA_REQUEST, MEDIA_UPDATE_FAIL, MEDIA_UPDATE_SENT, USER_MEDIA_REQUEST
} from "../actions/types";

export default (state = {
    mediasList: [],
    userMediasList: [],
    albums: []
}, action) => {

    switch (action.type) {
        case MEDIA_REQUEST: {
            return {...state, mediasList: action.payload};
        }
        case USER_MEDIA_REQUEST: {
            return {...state, userMediasList: action.payload};
        }
        case MEDIA_FAILURE: {
            return action.message;
        }
        case MEDIA_POST_SENT: {
            return {...state, mediaPostMessage: action.message, isPosting: true, mediaErrorMessage: ""};
        }
        case MEDIA_POST_SUCCESS: {
            return {...state, isPosting: false};
        }
        case MEDIA_POST_FAIL: {
            return {...state, mediaErrorMessage: action.message, isPosting: false, mediaPostMessage: ""};
        }
        case MEDIA_DELETE_DONE: {
            const mediaID = action.mediaDeleteID;
            return {...state, userMediasList: state.userMediasList.filter(media => media.id !== mediaID)};
        }
        case MEDIA_DELETE_FAIL: {
            return {...state, deleteError: action.message};
        }
        case MEDIA_UPDATE_SENT: {
            let userMediasList = state.userMediasList.map(media => changeMedia(media, action.media));
            
            if (state.mediasList) {
                return {...state, userMediasList, mediasList: state.mediasList.map(media => changeMedia(media, action.media))}
            }
            return {...state, userMediasList}
        }
        case MEDIA_UPDATE_FAIL: {
            return {...state, updateError: action.message}
        }
        case ALBUM_REQUEST: {
            return {...state, albums: action.payload};
        }
        case ALBUM_EDIT_PUT: {
            return {...state, albums: state.albums.map(mediaAlbum => {
                    if (mediaAlbum.album.id === action.album.id) {
                        mediaAlbum.album.name = action.album.name;
                        mediaAlbum.album.showUnlistedImages = action.album.showUnlistedImages;
                        return mediaAlbum;
                    }
                    return mediaAlbum;
                }),}
        }
        case ALBUM_FAILURE: {
            return action.message;
        }
        default: {
            return state;
        }
    }
};

const changeMedia = (media, action) => {
    if (media.id === action.media.id) {
        return action.media;
    }
    return media;
};