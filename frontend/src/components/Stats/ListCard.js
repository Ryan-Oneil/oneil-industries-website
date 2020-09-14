import { Card, List } from "antd";
import React from "react";

export default props => {
  return (
    <Card title={props.title} headStyle={{ color: "rgba(0, 0, 0, 0.45)" }}>
      <List {...props} />
    </Card>
  );
};
