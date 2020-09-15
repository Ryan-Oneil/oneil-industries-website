import React, { useEffect, useState } from "react";
import { Card, Divider, List, Tabs } from "antd";
import Media from "./Media";
import InfiniteScroll from "react-infinite-scroller";
import { fetchImages } from "../../reducers/mediaReducer";
import { useDispatch, useSelector } from "react-redux";
import VideoCameraOutlined from "@ant-design/icons/lib/icons/VideoCameraOutlined";
import FileImageOutlined from "@ant-design/icons/lib/icons/FileImageOutlined";
const { Meta } = Card;
const { TabPane } = Tabs;

export default ({ imageEndpoint, videoEndpoint, handleShowDialog }) => {
  const { medias } = useSelector(state => state.medias.entities);
  const [imageIds, setImageIds] = useState([]);
  const [imageList, setImageList] = useState([]);
  const [videoIds, setVideoIds] = useState([]);
  const [videoList, setVideoList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [imageTotal, setImageTotal] = useState(0);
  const [videoTotal, setVideoTotal] = useState(0);
  const dispatch = useDispatch();

  useEffect(() => {
    setImageList(imageIds.flatMap(id => (medias[id] ? medias[id] : [])));
  }, [imageIds, medias]);

  useEffect(() => {
    setVideoList(videoIds.flatMap(id => (medias[id] ? medias[id] : [])));
  }, [videoIds, medias]);

  useEffect(() => {
    handleImageInfiniteOnLoad(0);
    handleVideoInfiniteOnLoad(0);
  }, []);

  const handleImageInfiniteOnLoad = page => {
    setLoading(true);
    dispatch(fetchImages(imageEndpoint, page, 30)).then(({ payload }) => {
      if (!Array.isArray(payload.medias)) {
        return;
      }
      const payloadMediaIds = payload.medias.map(media => media.id);

      setImageIds(prevState => [...prevState, ...payloadMediaIds]);
      setImageTotal(payload.total);
      setLoading(false);
    });
  };

  const handleVideoInfiniteOnLoad = page => {
    setLoading(true);
    dispatch(fetchImages(videoEndpoint, page, 30)).then(({ payload }) => {
      if (!Array.isArray(payload.medias)) {
        return;
      }
      const payloadMediaIds = payload.medias.map(media => media.id);

      setVideoIds(prevState => [...prevState, ...payloadMediaIds]);
      setVideoTotal(payload.total);
      setLoading(false);
    });
  };

  const renderMediaList = (loadMore, hasMore, mediaList) => {
    return (
      <div style={{ height: "780px", overflow: "auto" }}>
        <InfiniteScroll
          initialLoad={false}
          pageStart={0}
          loadMore={loadMore}
          hasMore={!loading && hasMore}
          useWindow={false}
        >
          <List
            grid={{
              gutter: 16,
              xs: 1,
              sm: 2,
              md: 4,
              lg: 4,
              xl: 5,
              xxl: 5
            }}
            dataSource={mediaList}
            renderItem={item => (
              <List.Item key={item.id}>
                <Card>
                  <div className="pointerCursor">
                    <Media
                      media={item}
                      onClick={handleShowDialog.bind(this, item.id)}
                    />
                  </div>
                  <Divider />
                  <Meta
                    title={item.name}
                    description={item.dateAdded}
                    style={{ textAlign: "center" }}
                  />
                </Card>
              </List.Item>
            )}
          />
        </InfiniteScroll>
      </div>
    );
  };

  return (
    <div
      style={{
        paddingLeft: "30px",
        marginTop: "-10px"
      }}
    >
      <Tabs defaultActiveKey="1">
        <TabPane
          tab={
            <span>
              <FileImageOutlined />
              Images
            </span>
          }
          key="1"
        >
          {renderMediaList(
            handleImageInfiniteOnLoad,
            imageIds.length < imageTotal,
            imageList
          )}
        </TabPane>
        <TabPane
          tab={
            <span>
              <VideoCameraOutlined />
              Videos
            </span>
          }
          key="2"
        >
          {renderMediaList(
            handleVideoInfiniteOnLoad,
            videoIds.length < videoTotal,
            videoList
          )}
        </TabPane>
      </Tabs>
    </div>
  );
};
