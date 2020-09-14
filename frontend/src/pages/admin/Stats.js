import React, { useEffect, useState } from "react";
import { displayBytesInReadableForm } from "../../helpers";
import { Col, Row, List, Avatar } from "antd";
import StatisticCard from "../../components/Stats/StatisticCard";
import ListCard from "../../components/Stats/ListCard";
import { useDispatch, useSelector } from "react-redux";
import { getAdminStats } from "../../reducers/adminReducer";

export default () => {
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();

  const {
    totalMedia,
    totalAlbums,
    totalUsers,
    recentUsers,
    remainingStorage,
    usedStorage
  } = useSelector(state => state.admin.stats);

  useEffect(() => {
    dispatch(getAdminStats());
  }, []);

  return (
    <div style={{ padding: "24px" }}>
      <Row gutter={[32, 32]} type="flex">
        <Col xs={24} sm={24} md={6} lg={6} xl={4}>
          <StatisticCard title="Total Media" value={totalMedia} />
        </Col>
        <Col xs={24} sm={24} md={6} lg={6} xl={4}>
          <StatisticCard title="Total Albums" value={totalAlbums} />
        </Col>
        <Col xs={24} sm={24} md={6} lg={6} xl={4}>
          <StatisticCard title="Total Users" value={totalUsers} />
        </Col>
        <Col xs={24} sm={24} md={6} lg={6} xl={4}>
          <StatisticCard
            title="Used Storage"
            value={displayBytesInReadableForm(usedStorage)}
          />
        </Col>
        <Col xs={24} sm={24} md={6} lg={6} xl={4}>
          <StatisticCard
            title="Available Storage"
            value={displayBytesInReadableForm(remainingStorage)}
          />
        </Col>
        <Col xs={24} sm={24} md={6} lg={6} xl={4}>
          <StatisticCard
            title="Available Storage"
            value={displayBytesInReadableForm(remainingStorage)}
          />
        </Col>
      </Row>
      <Row gutter={[32, 32]} type="flex">
        <Col xs={24} sm={24} md={12} lg={8} xl={8}>
          <ListCard
            title="Recent Users"
            itemLayout="horizontal"
            dataSource={recentUsers}
            loading={loading}
            renderItem={item => (
              <List.Item>
                <List.Item.Meta
                  avatar={
                    <Avatar
                      src={require("../../assets/images/file.png")}
                      size="large"
                    />
                  }
                  title={item.name}
                  description={item.email}
                />
              </List.Item>
            )}
          />
        </Col>
      </Row>
    </div>
  );
};
