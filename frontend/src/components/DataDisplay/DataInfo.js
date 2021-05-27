import React from "react";

const DataInfo = ({ title, info }) => {
  return (
    <span>
      <span
        style={{
          fontWeight: 500,
          color: "rgba(55,65,81,var(--tw-text-opacity))",
          fontSize: "1.2em",
        }}
      >
        {title}
      </span>
      <p>{info}</p>
    </span>
  );
};
export default DataInfo;
