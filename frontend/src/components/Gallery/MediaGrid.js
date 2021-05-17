import React, { useEffect, useState } from "react";
import { Card, ConfigProvider, Empty, List } from "antd";
import InfiniteScroll from "react-infinite-scroller";
import { fetchMedia } from "../../reducers/mediaReducer";
import { useDispatch, useSelector } from "react-redux";

export default ({ mediaEndpoint, mediaCardLayout, height, style }) => {
  const { medias } = useSelector(state => state.medias.entities);
  const [mediaIds, setMediaIds] = useState([]);
  const [mediaList, setMediaList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [mediaTotal, setMediaTotal] = useState(0);
  const dispatch = useDispatch();

  const fetchNewImagesFromReduxStore = useEffect(() => {
    setMediaList(mediaIds.flatMap(id => (medias[id] ? medias[id] : [])));
  }, [mediaIds, medias]);

  const handleMediaInfiniteOnLoad = page => {
    setLoading(true);
    dispatch(fetchMedia(mediaEndpoint, page, 30)).then(({ payload }) => {
      if (!Array.isArray(payload.medias)) {
        return;
      }
      const payloadMediaIds = payload.medias.map(media => media.id);

      setMediaIds(prevState => [...prevState, ...payloadMediaIds]);
      setMediaTotal(payload.total);
      setLoading(false);
    });
  };

  const loadMediasOnLoad = useEffect(() => {
    handleMediaInfiniteOnLoad(0);
  }, []);

  const renderEmpty = () => {
    return (
      <div style={{ width: "20vw" }} className={"centerContent"}>
        <Card className={"roundedBorder extraPadding"}>
          {Empty.PRESENTED_IMAGE_DEFAULT}
          <p className={"removeMargin midText"}>No Media Found</p>
        </Card>
      </div>
    );
  };

  return (
    <ConfigProvider renderEmpty={renderEmpty}>
      <div
        style={{
          height,
          overflow: "auto",
          ...style
        }}
      >
        <InfiniteScroll
          initialLoad={false}
          pageStart={0}
          loadMore={handleMediaInfiniteOnLoad}
          hasMore={!loading && mediaIds.length < mediaTotal}
          useWindow={false}
        >
          <List
            grid={{
              gutter: 16,
              xs: 1,
              sm: 2,
              md: 3,
              lg: 3,
              xl: 5,
              xxl: 6
            }}
            style={{ marginLeft: "-8px" }}
            dataSource={mediaList}
            renderItem={item => (
              <List.Item key={item.id}>{mediaCardLayout(item)}</List.Item>
            )}
          />
        </InfiniteScroll>
      </div>
    </ConfigProvider>
  );
};
