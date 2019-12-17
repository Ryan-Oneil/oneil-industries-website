import React from 'react';
import {Link} from "react-router-dom";
import '../../assets/css/layout.css';

class DropDownNav extends React.Component {

    state = { showNavMenu: false};

    constructor(props) {
        super(props);

        this.setWrapperRef = this.setWrapperRef.bind(this);
        this.handleClickOutside = this.handleClickOutside.bind(this);
    }

    componentDidMount() {
        document.addEventListener('mousedown', this.handleClickOutside);
    }

    componentWillUnmount() {
        document.removeEventListener('mousedown', this.handleClickOutside);
    }

    handleClickOutside(event) {
        if (this.wrapperRef && !this.wrapperRef.contains(event.target)) {
            this.setState({showNavMenu: false});
        }
    }

    setWrapperRef(node) {
        this.wrapperRef = node;
    }

    render() {
        return (
            <div className="ui dropdown item" onMouseOver={() => this.setState({showNavMenu: true})}>
                <Link to="/images" className="" >Images<i className="dropdown icon"/></Link>

                {this.state.showNavMenu && <div className="menu showDisplay" ref={this.setWrapperRef} onMouseLeave={() => this.setState({showNavMenu: false})}>
                    <Link to="/images/upload" className="item">Upload</Link>
                    <Link to="/images/mygallery" className="item">My Images</Link>
                    <Link to="/images/myalbums" className="item">My Albums</Link>
                </div>}
            </div>
        )
    }
}
export default DropDownNav;