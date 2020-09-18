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
import BottomFooter from "./site layout/BottomFooter";
import { Layout } from "antd";
import Login from "../pages/auth/Login";
import PublicRoute from "./PublicRoute";
import Register from "../pages/auth/Register";
import ResetPassword from "../pages/auth/ResetPassword";
import ChangePassword from "../pages/auth/ChangePassword";
import EmailConfirmation from "../pages/auth/EmailConfirmation";
import SharedLinkPage from "../pages/UserDashboard/FileShare/SharedLinkPage";
const { Footer } = Layout;

class ClientApp extends React.Component {
  render() {
    return (
      <Layout className={"blueBackgroundColor"} style={{ height: "100vh" }}>
        <BrowserRouter>
          <NavMenu />
          <Layout style={{ background: "rgb(61, 76, 104)" }}>
            <Switch>
              <Route path="/" exact component={Home} />
              <Route path="/images" component={Images} exact />
              <Route path="/gallery/album/:albumName" component={Album} />
              <PublicRoute path="/login">
                <Route exact path="/login" component={Login} />
              </PublicRoute>
              <PublicRoute path="/register">
                <Route exact path="/register" component={Register} />
              </PublicRoute>
              <PublicRoute path="/confirmEmail/:token">
                <Route
                  exact
                  path="/confirmEmail/:token"
                  component={EmailConfirmation}
                />
              </PublicRoute>
              <PublicRoute path="/resetPassword">
                <Route exact path="/resetPassword" component={ResetPassword} />
              </PublicRoute>
              <PublicRoute path="/changePassword/:token">
                <Route
                  exact
                  path="/changePassword/:token"
                  component={ChangePassword}
                />
              </PublicRoute>
              <Route path="/shared/:id" component={SharedLinkPage} />
              <Route path="/services" exact component={Services} />
              <PrivateRoute path="/dashboard">
                <Route path="/dashboard" component={ProfileRouting} />
              </PrivateRoute>
              <PrivateRoute path="/admin">
                <Route path="/admin" component={Admin} />
              </PrivateRoute>
              <Route path="*">
                <NotFound />
              </Route>
            </Switch>
          </Layout>
        </BrowserRouter>
        {/*<Footer className="lightBlack bottomFooter">*/}
        {/*  <BottomFooter />*/}
        {/*</Footer>*/}
      </Layout>
    );
  }
}
export default ClientApp;
