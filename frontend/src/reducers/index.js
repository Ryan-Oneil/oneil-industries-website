import { combineReducers } from 'redux';
import { reducer as formReducer} from 'redux-form';
import  authReducer  from './authReducer';
import mediaReducer from "./mediaReducer";

export default combineReducers({
    auth: authReducer,
    form: formReducer,
    medias: mediaReducer
})

