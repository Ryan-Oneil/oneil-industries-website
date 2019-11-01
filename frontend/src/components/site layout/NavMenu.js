import React from 'react';
import {Link} from "react-router-dom";
import '../../assets/css/layout.css';
import {logoutUser} from "../../actions";
import DropDownNav from "./DropDownNav";

class NavMenu extends React.Component {
    render() {
        const { dispatch, isAuthenticated } = this.props;

        return (
            <div className="ui nav navColor inverted secondary pointing massive menu">
                <div className="ui container">
                    <Link to="/" className="item">
                        Home
                    </Link>
                    <Link to="/about" className="item">
                        About
                    </Link>
                    <Link to="/contact" className="item">
                        Contact
                    </Link>
                    <DropDownNav/>
                    <Link to="/services" className="item">
                        Services
                    </Link>

                    <div className="right menu">

                        {!isAuthenticated &&
                        <Link to="/login" className="item">
                            Login
                        </Link>
                        }
                        {isAuthenticated &&
                        <button className="item" onClick={() => (dispatch(logoutUser()))}>Logout</button>}
                    </div>
                </div>
            </div>
        );
    }
}
export default NavMenu;