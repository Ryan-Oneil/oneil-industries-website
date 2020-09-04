import { combineReducers } from "redux";
import authReducer from "./authReducer";
import mediaReducer from "./mediaReducer";
import contactReducer from "./contactReducer";
import serviceReducer from "./serviceReducer";
import profileReducer from "./profileReducer";
import adminReducer from "./adminReducer";
import globalErrorReducer from "./globalErrorReducer";

export default combineReducers({
  auth: authReducer,
  medias: mediaReducer,
  contact: contactReducer,
  services: serviceReducer,
  profile: profileReducer,
  admin: adminReducer,
  globalErrors: globalErrorReducer
});
