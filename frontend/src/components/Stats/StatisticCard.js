import React from "react";
import { Card, Statistic } from "antd";

export default props => {
  const { loading } = props;

  return (
    <Card loading={loading}>
      <Statistic {...props} />
    </Card>
  );
};
