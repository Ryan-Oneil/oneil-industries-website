import React from "react";
import { Col, Divider, Row, Space } from "antd";
import AudioOutlined from "@ant-design/icons/lib/icons/AudioOutlined";
import ToolOutlined from "@ant-design/icons/lib/icons/ToolOutlined";
import DatabaseOutlined from "@ant-design/icons/lib/icons/DatabaseOutlined";

const Home = () => {
  return (
    <div className="whiteText">
      <h1 className="centerText">
        Oneil Industries a community dedicated to banter
      </h1>

      <img
        src={require("../assets/images/oneilFactory.png")}
        className="centerContent imageResponse"
        alt="Factory"
      />

      <Divider style={{ color: "white" }}>
        Some of the things that makes us great
      </Divider>
      <Row
        justify="center"
        align="top"
        gutter={[16, { xs: 18, sm: 6, md: 6, lg: 8, xl: 5 }]}
      >
        <Col xs={18} sm={6} md={6} lg={7} xl={6} className={"fixGap"}>
          <div className="formatInfo">
            <h3>
              <Space>
                <AudioOutlined />
                Voice Services
              </Space>
            </h3>
            We provide whitelisted Teamspeak and Discord servers for all to use.
            Navigate to the services section of the website to connect to them.
          </div>
        </Col>
        <Col xs={18} sm={6} md={6} lg={7} xl={6}>
          <div className="formatInfo">
            <h3>
              <Space>
                <ToolOutlined />
                Tools
              </Space>
            </h3>
            Oneil Industries provides a wide variety of tools to its Members
            such as image gallery, game calculators, and more. All found under
            the dashboard section for registered users.
          </div>
        </Col>
        <Col xs={18} sm={6} md={6} lg={7} xl={6} className={"fixGap"}>
          <div className="formatInfo">
            <h3>
              <Space>
                <DatabaseOutlined />
                Game Servers
              </Space>
            </h3>
            <div>
              Game servers are spun up for the times we want to play games
              together that require servers such as survival games.
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default Home;
