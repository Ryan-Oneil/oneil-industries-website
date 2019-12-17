import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import Header from './site layout/Header';
import NavMenu from "./site layout/NavMenu";
import Footer from "./site layout/Footer";
import Home from "../pages/Home";
import Contact from "../pages/Contact";
import Login from "../pages/Login";
import Images from "../pages/Gallery/GalleryPage";
import Album from "../pages/Gallery/AlbumPage";
import NotFound from "../pages/NotFound";
import Services from "../pages/Services";
import ProfilePage from "../pages/Profile/ProfilePage";
import EmailConfirmationPage from "../pages/EmailConfirmationPage";
import NewPasswordForm from "./formElements/NewPasswordForm";
import Admin from "../pages/admin/Admin";
import PrivateRoute from "./PrivateRoute";

class App extends React.Component {

    render() {
        return (
            <div className="fullHeight">

                <div className="maindiv">
                    <BrowserRouter>
                        <div className="fixedHeader">
                            <Header/>
                            <NavMenu/>
                        </div>

                        <div className="mainpage">

                            <div>
                                <Switch>
                                    <Route path="/" exact component={Home}/>
                                    <Route path="/contact" exact component={Contact}/>
                                    <Route path="/images" component={Images}/>
                                    <Route path="/images/album/:albumName" component={Album} />
                                    <Route path="/login" component={Login}/>
                                    <Route path="/services" exact component={Services}/>
                                    <PrivateRoute>
                                        <Route path="/profile" component={ProfilePage}/>
                                        <Route path="/admin" component={Admin}/>
                                    </PrivateRoute>
                                    <Route path="/confirmEmail/:token" render={(props) => (
                                        <EmailConfirmationPage
                                            {...props}
                                            endpoint="/auth/registrationConfirm/"/>)}
                                    />
                                    <Route path="/services/confirm/:token" render={(props) => (
                                        <EmailConfirmationPage
                                            {...props}
                                            endpoint="/services/user/confirm/"/>)}
                                    />
                                    <Route path="/resetPassword/:token"
                                           component={NewPasswordForm}
                                    />
                                    <Route path="*">
                                        <NotFound/>
                                    </Route>
                                </Switch>
                            </div>

                    </div>
                    </BrowserRouter>
                </div>
                <Footer/>
            </div>
        );
    }
}
export default App;
