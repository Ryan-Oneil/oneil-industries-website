import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { getAllUsers } from "../../reducers/adminReducer";
import { Button, Card, Table, Tooltip } from "antd";
import EditOutlined from "@ant-design/icons/lib/icons/EditOutlined";

export default props => {
  const { match } = props;
  const dispatch = useDispatch();
  const { users } = useSelector(state => state.admin);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (users.length === 0) {
      setLoading(true);
      dispatch(getAllUsers()).then(() => setLoading(false));
    }
  }, []);

  const columns = [
    {
      title: "ID",
      dataIndex: "id",
      sorter: true,
      defaultSortOrder: "descend"
    },
    {
      title: "Username",
      dataIndex: "name"
    },
    {
      title: "Email",
      dataIndex: "email"
    },
    {
      title: "Role",
      dataIndex: "role",
      sorter: true
    },
    {
      title: "Status",
      dataIndex: "enabled",
      render: enabled => (enabled ? "Enabled" : "Disabled"),
      sorter: true
    },
    {
      title: "",
      key: "action",
      render: (text, record) => (
        <Tooltip title="Edit">
          <Link to={`${match.path}/${record.name}`}>
            <Button shape="circle" icon={<EditOutlined />} />
          </Link>
        </Tooltip>
      )
    }
  ];

  return (
    <div style={{ padding: "24px" }}>
      <Card>
        <Table
          dataSource={users}
          columns={columns}
          rowKey={user => user.id}
          loading={loading}
        />
      </Card>
    </div>
  );
};
