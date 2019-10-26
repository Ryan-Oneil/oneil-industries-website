import React from 'react';
import {Link} from "react-router-dom";
import '../../assets/css/layout.css';

class DropDownNav extends React.Component {
    state = { showNavMenu: false};

    render() {
        return (
            <div className="ui showDisplay pointing dropdown link" onMouseOver={() => this.setState({showNavMenu: true})}>
                <Link to="/images" className="item" >Images<i className="dropdown icon removeMargin"/></Link>

                {this.state.showNavMenu && <div className="menu showDisplay" onMouseLeave={() => this.setState({showNavMenu: false})}>
                    <Link to="/images/upload" className="item">Upload</Link>
                    <Link to="/images/mygallery" className="item">My Images</Link>
                    <Link to="/images/myalbums" className="item">My Albums</Link>
                </div>}
            </div>
        )
    }
}
export default DropDownNav;