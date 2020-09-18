import React, { useEffect, useState } from "react";
import { displayBytesInReadableForm } from "../../helpers";
import { Button, Card, Tooltip } from "antd";
import EyeOutlined from "@ant-design/icons/lib/icons/EyeOutlined";
import { Link } from "react-router-dom";
import EditOutlined from "@ant-design/icons/lib/icons/EditOutlined";
import ConfirmButton from "../ConfirmButton";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import PaginationTable from "./PaginationTable";
import { useDispatch, useSelector } from "react-redux";
import { deleteLink } from "../../reducers/fileReducer";

export default ({ editPath, fetchData }) => {
  const dispatch = useDispatch();
  const [linkIds, setLinkIds] = useState([]);
  const [totalLinks, setTotalLinks] = useState(0);
  const [linkData, setLinkData] = useState([]);
  const { links } = useSelector(state => state.fileSharer.entities);

  useEffect(() => {
    let data = [];

    linkIds.forEach(id => {
      if (links[id]) {
        data.push(links[id]);
      }
    });
    setLinkData(data);
  }, [linkIds, links]);

  const columns = [
    {
      title: "Created",
      dataIndex: "creationDate",
      sorter: true,
      defaultSortOrder: "descend"
    },
    {
      title: "Title",
      dataIndex: "title",
      render: name => (name ? name : "N/A")
    },
    {
      title: "Expires",
      dataIndex: "expiryDatetime",
      sorter: true
    },
    {
      title: "Views",
      dataIndex: "views",
      sorter: true
    },
    {
      title: "Size",
      dataIndex: "size",
      render: size => displayBytesInReadableForm(size),
      sorter: true
    },
    {
      title: "",
      key: "action",
      render: (text, record) => (
        <>
          <Tooltip title="View">
            <Button
              shape="circle"
              icon={<EyeOutlined />}
              onClick={() => {
                window.open(`/shared/${record.id}`, "_blank");
              }}
            />
          </Tooltip>
          <Tooltip title="Edit">
            <Link to={`${editPath}/edit/${record.id}`}>
              <Button shape="circle" icon={<EditOutlined />} />
            </Link>
          </Tooltip>
          <ConfirmButton
            toolTip="Delete"
            shape="circle"
            buttonIcon={<DeleteOutlined />}
            confirmAction={() => dispatch(deleteLink(record.id))}
            modalTitle="Do you want to delete this link?"
            modalDescription="All files will also be deleted"
          />
        </>
      )
    }
  ];

  return (
    <PaginationTable
      totalData={totalLinks}
      data={linkData}
      columns={columns}
      fetchData={(page, size, sorter) =>
        dispatch(fetchData(page, size, sorter)).then(({ payload }) => {
          setLinkIds(payload.links.map(link => link.id));
          setTotalLinks(payload.total);
        })
      }
      defaultSort={{ field: "creationDate", order: "descend" }}
    />
  );
};
