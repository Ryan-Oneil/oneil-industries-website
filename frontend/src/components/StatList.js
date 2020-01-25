import React from "react";

export const StatList = props => {
  return (
    <div className="four wide column">
      <div className="ui fluid  card">
        <div className="content">
          <div className="header">{props.header}</div>
        </div>
        <div className="content">{props.children}</div>
      </div>
    </div>
  );
};

export const ListItem = ({ headerText, subText, iconType, src }) => {
  return (
    <div className="item">
      <img className="ui avatar image" src={src} alt={`${iconType} icon`} />
      <div className="content">
        <div className="header">{headerText}</div>
        {subText}
      </div>
    </div>
  );
};
