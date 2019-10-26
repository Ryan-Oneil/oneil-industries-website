import React from 'react';
import { BrowserRouter, Route} from 'react-router-dom';

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
                            <div className="ui container">
                                <Route path="/" exact component={Home}/>
                                <Route path="/about" exact component={About}/>
                                <Route path="/contact" exact component={Contact}/>
                                <Route path="/images" exact render={() => (
                                        <Images
                                        dispatch={dispatch}
                                    />)
                                }/>
                                <Route path="/images/upload" exact render={() => (
                                    <UploadPage
                                        dispatch={dispatch}
                                        user={user}
                                    />)
                                }/>
                                <Route path="/login" render={(props) => (
                                    <Login
                                        {...props}
                                        dispatch={dispatch}
                                        errorMessage={errorMessage}
                                        isAuthenticated={isAuthenticated}
                                    />)
                                }/>
                            </div>
                        </BrowserRouter>
                    </div>
                </div>

                <Footer/>
            </div>
        );
    }
}

function mapStateToProps(state) {

    const { auth } = state;
    const { isAuthenticated, errorMessage, user } = auth;

    return {
        isAuthenticated,
        errorMessage,
        user
    }
}

export default connect(mapStateToProps)(App);
