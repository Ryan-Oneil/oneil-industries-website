import React, { useEffect, useState } from "react";
import { Table } from "antd";

export default props => {
  const { totalData, fetchData, data, columns, defaultSort } = props;
  const [loading, setLoading] = useState(true);
  const [sort, setSort] = useState(defaultSort);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
    total: totalData
  });

  const loadData = ({ current, pageSize }, sorter) => {
    setLoading(true);

    //current is always reduced by 1 since backend starts page at 0 while frontend starts at 1
    //Also fetches 10 extra rows
    fetchData(current - 1, pageSize + 10, sorter).then(() => setLoading(false));
  };

  //This will load new data when a row has been deleted
  useEffect(() => {
    if (
      data.length < pagination.pageSize &&
      data.length !== totalData &&
      !loading
    ) {
      loadData(pagination, sort);
    }
  }, [data]);

  useEffect(() => {
    loadData(pagination, defaultSort);
  }, []);

  useEffect(() => {
    setPagination(prevState => {
      return { ...prevState, total: totalData };
    });
  }, [totalData]);

  const handleTableChange = (pagination, filters, sorter) => {
    setPagination(pagination);
    setSort(sorter);
    loadData(pagination, sorter);
  };

  return (
    <Table
      dataSource={data}
      columns={columns}
      rowKey={item => item.id}
      pagination={pagination}
      loading={loading}
      onChange={handleTableChange}
    />
  );
};
