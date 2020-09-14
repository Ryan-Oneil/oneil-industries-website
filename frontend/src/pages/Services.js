import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import ServiceList from "../components/Services/ServiceList";
import { Card, Col, Row } from "antd";
import {
  getDiscordActiveList,
  getTeamspeakActiveList
} from "../reducers/serviceReducer";

export default () => {
  const dispatch = useDispatch();
  const { activeTSList, activeDiscord } = useSelector(state => state.services);

  useEffect(() => {
    dispatch(getTeamspeakActiveList());
    dispatch(getDiscordActiveList());
  }, []);

  return (
    <Row justify="center" gutter={[32, 32]} style={{ marginTop: "20px" }}>
      <Col xs={18} sm={18} md={16} lg={10} xl={8}>
        <Card
          title={
            <h1 className="centerText">
              <a href="ts3server://ts.oneilindustries.biz/?port=9987">
                Connect to Teamspeak
              </a>
            </h1>
          }
        >
          <ServiceList serviceList={activeTSList} />
        </Card>
      </Col>
      <Col xs={18} sm={18} md={16} lg={10} xl={8}>
        <Card
          title={
            <h1 className="centerText">
              <a href="https://discord.gg/dZSKaaX">Connect to Discord</a>
            </h1>
          }
        >
          <ServiceList serviceList={activeDiscord} />
        </Card>
      </Col>
    </Row>
  );
};
