import React, { useEffect, useState } from "react";
import { Avatar, List, Col, Row } from "antd";
import { displayBytesInReadableForm } from "../../helpers";
import { getUserDetails } from "../../reducers/userReducer";
import { useDispatch, useSelector } from "react-redux";
import { getUserLinkStats } from "../../reducers/fileReducer";
import StatisticCard from "../../components/Stats/StatisticCard";
import ListCard from "../../components/Stats/ListCard";
import { getUserMediaStats } from "../../reducers/mediaReducer";
import { BASE_URL } from "../../apis/api";
import FileZipOutlined from "@ant-design/icons/lib/icons/FileZipOutlined";

export default () => {
  const dispatch = useDispatch();
  const { name } = useSelector((state) => state.auth.user);
  const { used, max } = useSelector((state) => state.user.storageQuota);
  const { totalViews, recentLinks, totalFiles } = useSelector(
    (state) => state.fileSharer.stats
  );
  const { totalMedias, recentMedias } = useSelector(
    (state) => state.medias.stats
  );
  const [loadingData, setLoadingData] = useState(true);
  const [loadingMediaData, setLoadingMediaData] = useState(true);

  //Loads most recent quota count
  useEffect(() => {
    dispatch(getUserDetails(name));
    dispatch(getUserLinkStats(name)).then(() => setLoadingData(false));
    dispatch(getUserMediaStats(name)).then(() => setLoadingMediaData(false));
  }, []);

  return (
    <>
      <Row gutter={[32, 32]}>
        <Col xs={12} sm={12} md={8} lg={8} xl={6}>
          <StatisticCard title="Total Shared Files" value={totalFiles} />
        </Col>
        <Col xs={12} sm={12} md={8} lg={8} xl={6}>
          <StatisticCard
            title="Used Storage Space"
            value={displayBytesInReadableForm(used)}
            suffix={`/ ${max} GB`}
          />
        </Col>
        <Col xs={12} sm={12} md={8} lg={8} xl={6}>
          <StatisticCard title="Total Link Views" value={totalViews} />
        </Col>
        <Col xs={12} sm={12} md={8} lg={8} xl={6}>
          <StatisticCard title="Total Medias" value={totalMedias} />
        </Col>
        <Col xs={24} sm={24} md={12} lg={12} xl={12}>
          <ListCard
            title="Recent Links"
            itemLayout="horizontal"
            loading={loadingData}
            dataSource={recentLinks}
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
        <Col xs={24} sm={24} md={12} lg={12} xl={12}>
          <ListCard
            title="Recent Medias"
            itemLayout="horizontal"
            loading={loadingMediaData}
            dataSource={recentMedias}
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
      </Row>
    </>
  );
};
