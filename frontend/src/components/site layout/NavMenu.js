import React from "react";
import { NavLink } from "react-router-dom";
import { Layout } from "antd";
import MainNav from "./MainNav";
import AuthNav from "./AuthNav";
import ResponsiveMainNav from "./ResponsiveMainNav";
const { Header } = Layout;

export default () => {
  return (
    <Header className="whiteBackground headerLogo">
      <div className="logo">
        <NavLink to="/">
          <img
            src={require("../../assets/images/OneilEnterpriseLogo.png")}
            alt="Factory"
            height={60}
          />
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
