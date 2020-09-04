import React from "react";
import { NavLink } from "react-router-dom";
import { Layout, Space } from "antd";
import MainNav from "./MainNav";
import AuthNav from "./AuthNav";
import ResponsiveMainNav from "./ResponsiveMainNav";
const { Header } = Layout;

export default () => {
  return (
    <Header className="whiteBackground">
      <div className="logo">
        <NavLink to="/">
          <h2 className="logoText centerItem">
            <Space>
              <img
                src={require("../../assets/images/fact.png")}
                alt="Factory"
                width={45}
                height={45}
              />
              Oneil Industries
            </Space>
          </h2>
        </NavLink>
      </div>
      <div className="leftMenu">
        <MainNav menuDirection={"horizontal"} />
      </div>
      <div className="rightMenu">
        <AuthNav menuDirection={"horizontal"} />
      </div>
      <ResponsiveMainNav />
    </Header>
  );
};
