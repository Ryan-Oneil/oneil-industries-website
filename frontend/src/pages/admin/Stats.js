import React, { useEffect, useState } from "react";
import { displayBytesInReadableForm } from "../../helpers";
import { Col, Row, List, Avatar } from "antd";
import StatisticCard from "../../components/Stats/StatisticCard";
import ListCard from "../../components/Stats/ListCard";
import { useDispatch, useSelector } from "react-redux";
import { getAdminLinkStats, getAdminStats } from "../../reducers/adminReducer";
import { BASE_URL } from "../../apis/api";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import FileZipOutlined from "@ant-design/icons/lib/icons/FileZipOutlined";
import { Link } from "react-router-dom";

export default () => {
  const [loading, setLoading] = useState(true);
  const [loadingFileShareData, setLoadingFileShareData] = useState(true);
  const dispatch = useDispatch();
  const { admin } = useSelector((state) => state);
  const { totalViews, totalLinks, mostViewed, recentShared } = admin.fileShare;

  const {
    totalMedia,
    totalAlbums,
    totalUsers,
    recentUsers,
    remainingStorage,
    usedStorage,
    recentMedia,
  } = useSelector((state) => state.admin.stats);

  useEffect(() => {
    dispatch(getAdminStats()).then(() => setLoading(false));
    dispatch(getAdminLinkStats()).then(() => setLoadingFileShareData(false));
  }, []);

  return (
    <div className="extraPadding">
      <Row gutter={[32, 32]} type="flex">
        <Col xs={12} sm={12} md={8} lg={8} xl={8}>
          <StatisticCard title="Total Media" value={totalMedia} />
        </Col>
        <Col xs={12} sm={12} md={8} lg={8} xl={8}>
          <StatisticCard title="Total Albums" value={totalAlbums} />
        </Col>
        <Col xs={12} sm={12} md={8} lg={8} xl={8}>
          <StatisticCard title="Total Users" value={totalUsers} />
        </Col>
        <Col xs={12} sm={12} md={8} lg={8} xl={8}>
          <StatisticCard
            title="Used Storage"
            value={displayBytesInReadableForm(usedStorage)}
            suffix={` / ${displayBytesInReadableForm(remainingStorage * 1000)}`}
          />
        </Col>
        <Col xs={12} sm={12} md={8} lg={8} xl={8}>
          <StatisticCard title="Total Shared Links" value={totalLinks} />
        </Col>
        <Col xs={12} sm={12} md={8} lg={8} xl={8}>
          <StatisticCard title="Total Views" value={totalViews} />
        </Col>
        <Col xs={24} sm={24} md={12} lg={12} xl={8}>
          <ListCard
            title="Recent Users"
            itemLayout="horizontal"
            dataSource={recentUsers}
            loading={loading}
            renderItem={(item) => (
              <List.Item>
                <Link to={`/admin/users/${item.name}`}>
                  <List.Item.Meta
                    avatar={<Avatar icon={<UserOutlined />} size="large" />}
                    title={item.name}
                    description={item.email}
                  />
                </Link>
              </List.Item>
            )}
          />
        </Col>
        <Col xs={24} sm={24} md={12} lg={12} xl={8}>
          <ListCard
            title="Recent Media"
            itemLayout="horizontal"
            dataSource={recentMedia}
            loading={loading}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  avatar={
                    <Avatar
                      src={`${BASE_URL}/gallery/image/thumbnail/${item.fileName}`}
                      size="large"
                    />
                  }
                  title={
                    <a
                      href={`${BASE_URL}/gallery/${item.mediaType.toLowerCase()}/${
                        item.fileName
                      }`}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      {item.fileName}
                    </a>
                  }
                  description={item.mediaType.toLowerCase()}
                />
              </List.Item>
            )}
          />
        </Col>
        <Col xs={24} sm={24} md={12} lg={12} xl={8}>
          <ListCard
            title="Recent Links"
            itemLayout="horizontal"
            dataSource={recentShared}
            loading={loadingFileShareData}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  avatar={<Avatar icon={<FileZipOutlined />} size="large" />}
                  title={
                    <a
                      href={`/shared/${item.id}`}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      {item.title ? item.title : item.id}
                    </a>
                  }
                  description={`${item.views} views`}
                />
              </List.Item>
            )}
          />
        </Col>
      </Row>
    </div>
  );
};
