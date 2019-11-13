import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import Header from './site layout/Header';
import NavMenu from "./site layout/NavMenu";
import Footer from "./site layout/Footer";
import Home from "./pages/Home";
import About from "./pages/About";
import Contact from "./pages/Contact";
import Login from "./pages/Login";
import {connect} from "react-redux";
import Images from "./pages/Gallery/GalleryPage";
import UploadPage from "./pages/Gallery/UploadPage";
import UserGalleryPage from "./pages/Gallery/UserGalleryPage";
import {PrivateRoute} from "./PrivateRoute";
import Album from "./pages/Gallery/AlbumPage";
import UserAlbumsPage from "./pages/Gallery/UserAlbumsPage";
import NotFound from "./pages/NotFound";
import Services from "./pages/Services";
import ProfilePage from "./pages/ProfilePage";

class App extends React.Component {

    render() {
        const { dispatch, isAuthenticated, errorMessage, user } = this.props;
        return (
            <div className="fullHeight">
                <Header/>
                <div className="maindiv">
                    <div className="mainpage">
                        <BrowserRouter>
                            <NavMenu
                                isAuthenticated={isAuthenticated}
                                dispatch={dispatch}
                            />
                            <div>
                                <Switch>
                                <Route path="/" exact component={Home}/>
                                <Route path="/about" exact component={About}/>
                                <Route path="/contact" exact component={Contact}/>
                                <Route path="/images" exact render={() => (
                                        <Images/>)
                                }/>
                                <PrivateRoute path="/images/upload" exact
                                              component={UploadPage}
                                              isAuthenticated={isAuthenticated}
                                              user={user}
                                />
                                <PrivateRoute path="/images/mygallery" exact
                                              component={UserGalleryPage}
                                              isAuthenticated={isAuthenticated}
                                              user={user}
                                />
                                <PrivateRoute path="/images/myalbums" exact
                                              component={UserAlbumsPage}
                                              isAuthenticated={isAuthenticated}
                                              user={user}
                                />
                                <Route path="/images/album/:albumName" component={Album} />
                                <Route path="/login" render={(props) => (
                                    <Login
                                        {...props}
                                        dispatch={dispatch}
                                        errorMessage={errorMessage}
                                        isAuthenticated={isAuthenticated}
                                    />)
                                }/>
                                <Route path="/services" exact component={Services}/>
                                    <PrivateRoute path="/profile" exact
                                                  component={ProfilePage}
                                                  isAuthenticated={isAuthenticated}
                                                  user={user}
                                    />
                                <Route path="*">
                                    <NotFound/>
                                </Route>
                                </Switch>
                            </div>
                        </BrowserRouter>
                    </div>
                </div>
                <Footer/>
            </div>
        );
    }
}
const mapStateToProps = (state) => {
    const { auth } = state;
    const { isAuthenticated, errorMessage, user } = auth;

    return {
        isAuthenticated,
        errorMessage,
        user
    }
};

export default connect(mapStateToProps)(App);
