import React from "react";

export default ({ header, value }) => {
  return (
    <div className="four wide column">
      <div className="ui segment">
        <div className="ui statistic wordWrap">
          <div className="value">{value}</div>
          <div className="label">{header}</div>
        </div>
      </div>
    </div>
  );
};
