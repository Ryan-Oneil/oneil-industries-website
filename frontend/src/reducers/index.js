import { combineReducers } from "redux";
import authReducer from "./authReducer";
import mediaReducer from "./mediaReducer";
import serviceReducer from "./serviceReducer";
import adminReducer from "./adminReducer";
import globalErrorReducer from "./globalErrorReducer";
import userReducer from "./userReducer";
import fileReducer from "./fileReducer";

export default combineReducers({
  auth: authReducer,
  medias: mediaReducer,
  services: serviceReducer,
  admin: adminReducer,
  user: userReducer,
  globalErrors: globalErrorReducer,
  fileSharer: fileReducer
});
