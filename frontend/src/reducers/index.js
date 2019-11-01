import { combineReducers } from 'redux';
import { reducer as formReducer} from 'redux-form';
import  authReducer  from './authReducer';
import mediaReducer from "./mediaReducer";
import contactReducer from "./contactReducer";

export default combineReducers({
    auth: authReducer,
    form: formReducer,
    medias: mediaReducer,
    contact: contactReducer
})

