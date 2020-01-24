import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import NavMenu from "./site layout/NavMenu";
import Home from "../pages/Home";
import Contact from "../pages/Contact";
import Images from "../pages/Gallery/Gallery";
import Album from "../pages/Gallery/AlbumPage";
import Login from "../pages/Login";
import Services from "../pages/Services";
import EmailConfirmationPage from "../pages/EmailConfirmationPage";
import NewPasswordForm from "./formElements/NewPasswordForm";
import PrivateRoute from "./PrivateRoute";
import ProfileRouting from "../pages/Profile/ProfileRouting";
import Admin from "../pages/admin/AdminRouting";
import NotFound from "../pages/NotFound";
import Footer from "./site layout/Footer";

class ClientApp extends React.Component {
  render() {
    return (
      <div className="maindiv">
        <BrowserRouter>
          <div className="fixedHeader">
            <NavMenu />
          </div>

          <div className="mainpage">
            <Switch>
              <Route path="/" exact component={Home} />
              <Route path="/contact" exact component={Contact} />
              <Route path="/images" component={Images} exact />
              <Route path="/gallery/album/:albumName" component={Album} />
              <Route path="/login" component={Login} />
              <Route path="/services" exact component={Services} />

              <Route
                path="/confirmEmail/:token"
                render={props => (
                  <EmailConfirmationPage
                    {...props}
                    endpoint="/auth/registrationConfirm/"
                  />
                )}
              />
              <Route
                path="/services/confirm/:token"
                render={props => (
                  <EmailConfirmationPage
                    {...props}
                    endpoint="/services/user/confirm/"
                  />
                )}
              />
              <Route path="/resetPassword/:token" component={NewPasswordForm} />
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
          </div>
        </BrowserRouter>
        <Footer />
      </div>
    );
  }
}
export default ClientApp;
