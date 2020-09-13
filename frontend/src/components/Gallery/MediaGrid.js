import React, { useEffect, useState } from "react";
import { Card, Divider, List } from "antd";
import Media from "./Media";
import InfiniteScroll from "react-infinite-scroller";
import { fetchImages } from "../../reducers/mediaReducer";
import { useDispatch, useSelector } from "react-redux";
const { Meta } = Card;

export default ({ endpoint, handleShowDialog }) => {
  const { medias } = useSelector(state => state.medias.entities);
  const [mediaIds, setMediaIds] = useState([]);
  const [mediaList, setMediaList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [total, setTotal] = useState(0);
  const dispatch = useDispatch();

  useEffect(() => {
    setMediaList(mediaIds.map(id => medias[id]));
  }, [mediaIds]);

  useEffect(() => {
    handleInfiniteOnLoad(0);
  }, []);

  const handleInfiniteOnLoad = page => {
    setLoading(true);
    dispatch(fetchImages(endpoint, page, 30)).then(({ payload }) => {
      const payloadMediaIds = payload.medias.map(media => media.id);

      setMediaIds(prevState => [...prevState, ...payloadMediaIds]);
      setTotal(payload.total);
      setLoading(false);
    });
  };

  return (
    <div style={{ height: "100%", overflow: "auto", paddingLeft: "30px" }}>
      <InfiniteScroll
        initialLoad={false}
        pageStart={0}
        loadMore={handleInfiniteOnLoad}
        hasMore={!loading && mediaIds.length < total}
        useWindow={false}
      >
        <List
          grid={{ gutter: 32, column: 4 }}
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
