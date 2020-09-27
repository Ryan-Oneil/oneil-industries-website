import React from "react";
import { Link, useLocation } from "react-router-dom";
import { Button, Card, Result } from "antd";
export default () => {
  let location = useLocation();

  return (
    <div className="extraPadding">
      <Card>
        <Result
          status="404"
          title="404"
          subTitle={`Sorry, ${location.pathname} does not exist.`}
          extra={
            <Link to="/">
              <Button type="primary">Back Home</Button>
            </Link>
          }
        />
      </Card>
    </div>
  );
};
