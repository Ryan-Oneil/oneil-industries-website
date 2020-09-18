import React from "react";
import { Card } from "antd";
import { getAllLinksPageable } from "../../../reducers/fileReducer";
import SharedLinkTable from "../../../components/Table/SharedLinkTable";

export default props => {
  const { match } = props;

  return (
    <div className="extraPadding">
      <Card>
        <SharedLinkTable
          editPath={match.path}
          fetchData={getAllLinksPageable}
          defaultSort={{ field: "creationDate", order: "descend" }}
        />
      </Card>
    </div>
  );
};
