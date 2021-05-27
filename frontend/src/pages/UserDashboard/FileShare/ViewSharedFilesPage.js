import React from "react";
import { Card } from "antd";
import { useSelector } from "react-redux";
import { getUserLinks } from "../../../reducers/fileReducer";
import SharedLinkTable from "../../../components/Table/SharedLinkTable";

export default (props) => {
  const { name } = useSelector((state) => state.auth.user);
  const { match } = props;

  return (
    <Card title="My Shared Links">
      <SharedLinkTable
        editPath={match.path}
        fetchData={(page, size, sorter) =>
          getUserLinks(name, page, size, sorter)
        }
      />
    </Card>
  );
};
