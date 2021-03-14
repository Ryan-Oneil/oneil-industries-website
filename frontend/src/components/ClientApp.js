import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import NavMenu from "./site layout/NavMenu";
import Home from "../pages/Home";
import Images from "../pages/Gallery/Gallery";
import Album from "../pages/Gallery/AlbumPage";
import Services from "../pages/Services";
import PrivateRoute from "./PrivateRoute";
import ProfileRouting from "../pages/UserDashboard/Routing";
import Admin from "../pages/admin/AdminRouting";
import NotFound from "../pages/NotFound";
import { Layout } from "antd";
import Login from "../pages/auth/Login";
import PublicRoute from "./PublicRoute";
import Register from "../pages/auth/Register";
import ResetPassword from "../pages/auth/ResetPassword";
import ChangePassword from "../pages/auth/ChangePassword";
import EmailConfirmation from "../pages/auth/EmailConfirmation";
import SharedLinkPage from "../pages/UserDashboard/FileShare/SharedLinkPage";
import {
  ADMIN_BASE_URL,
  CHANGE_PASSWORD_URL,
  CONFIRM_EMAIL_URL,
  DASHBOARD,
  GALLERY_ALBUM_URL,
  GALLERY_URL,
  HOME_URL,
  LOGIN_URL,
  REGISTER_URL,
  RESET_PASSWORD_URL,
  SHARED_FILES_LINK_URL,
  VOICE_SERVICES_URL
} from "../constants/constants";

class ClientApp extends React.Component {
  render() {
    return (
      <Layout style={{ height: "100vh" }}>
        <BrowserRouter>
          <NavMenu />
          <Layout className={"mainBackgroundColor"}>
            <Switch>
              <Route path={HOME_URL} exact component={Home} />
              <Route path={GALLERY_URL} component={Images} exact />
              <Route path={GALLERY_ALBUM_URL} component={Album} />
              <PublicRoute path={LOGIN_URL}>
                <Route exact path={LOGIN_URL} component={Login} />
              </PublicRoute>
              <PublicRoute path={REGISTER_URL}>
                <Route exact path={REGISTER_URL} component={Register} />
              </PublicRoute>
              <PublicRoute path={CONFIRM_EMAIL_URL}>
                <Route
                  exact
                  path={CONFIRM_EMAIL_URL}
                  component={EmailConfirmation}
                />
              </PublicRoute>
              <PublicRoute path={RESET_PASSWORD_URL}>
                <Route
                  exact
                  path={RESET_PASSWORD_URL}
                  component={ResetPassword}
                />
              </PublicRoute>
              <PublicRoute path={CHANGE_PASSWORD_URL}>
                <Route
                  exact
                  path={CHANGE_PASSWORD_URL}
                  component={ChangePassword}
                />
              </PublicRoute>
              <Route path={SHARED_FILES_LINK_URL} component={SharedLinkPage} />
              <Route path={VOICE_SERVICES_URL} exact component={Services} />
              <PrivateRoute path={DASHBOARD}>
                <Route path={DASHBOARD} component={ProfileRouting} />
              </PrivateRoute>
              <PrivateRoute path={ADMIN_BASE_URL}>
                <Route path={ADMIN_BASE_URL} component={Admin} />
              </PrivateRoute>
              <Route path="*">
                <NotFound />
              </Route>
            </Switch>
          </Layout>
        </BrowserRouter>
      </Layout>
    );
  }
}
export default ClientApp;
