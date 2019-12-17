import React from "react";

class SideBarNav extends React.Component {

    render() {
        const {headerText, headerIcon} = this.props;
        return (
            <div className="three wide tablet only two wide computer only column removeTopPadding" id="dashboardSideMenu">
                <div className="dashboardHeader">
                    <i className={`icon ${headerIcon}`}/>{headerText}
                </div>
                <div className="ui vertical borderless fluid text menu" >
                    {this.props.children}
                </div>
            </div>
        )
    }
}
export default SideBarNav;