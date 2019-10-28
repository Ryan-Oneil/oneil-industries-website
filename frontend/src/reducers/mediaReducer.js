import {
    ALBUM_EDIT_PUT,
    ALBUM_FAILURE,
    ALBUM_REQUEST, MEDIA_DELETE_DONE, MEDIA_DELETE_FAIL,
    MEDIA_FAILURE,
    MEDIA_POST_FAIL,
    MEDIA_POST_SENT,
    MEDIA_POST_SUCCESS,
    MEDIA_REQUEST, USER_MEDIA_REQUEST
} from "../actions";

export default (state = [], action) => {

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
            return {...state, mediaPostMessage: action.message, isPosting: true};
        }
        case MEDIA_POST_SUCCESS: {
            return {...state, isPosting: false};
        }
        case MEDIA_POST_FAIL: {
            return {...state, mediaPostMessage: action.message, isPosting: false};
        }
        case MEDIA_DELETE_DONE: {
            const mediaID = action.mediaDeleteID;
            return {...state, mediasList: state.mediasList.filter(media => media.id !== mediaID)};
        }
        case MEDIA_DELETE_FAIL: {
            return {...state, deleteError: action.message};
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