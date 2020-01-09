import React from "react";

class SubNavMenu extends React.Component {
  state = { showList: false };

  render() {
    let direction = this.state.showList ? "down" : "left";

    return (
      <div className="removePadding removeMargin item">
        <div
          className={`subNavHeader ${
            this.state.showList ? "subMenuActive" : ""
          }`}
          onClick={() => this.setState({ showList: !this.state.showList })}
        >
          <i className={`icon ${this.props.icon}`} />
          {this.props.header}
          <i className={`angle ${direction} icon rightIcon`} />
        </div>
        {this.state.showList && (
          <div className="menu subMenuActive">{this.props.children}</div>
        )}
      </div>
    );
  }
}
export default SubNavMenu;
