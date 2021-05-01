import React from "react";
import { Menu } from "antd";
import { NavLink, withRouter } from "react-router-dom";
import SubMenu from "antd/es/menu/SubMenu";

const SideNav = ({ path, location, links = [], title, subLinks = [] }) => {
  const renderLink = (link, backgroundColour) => {
    return (
      <Menu.Item
        key={`${path}${link.path}`}
        style={{ backgroundColor: backgroundColour }}
      >
        <NavLink to={`${path}${link.path}`} exact>
          {link.icon}
          {link.name}
        </NavLink>
      </Menu.Item>
    );
  };

  const renderLinks = backgroundColor => {
    return links.map(link => renderLink(link, backgroundColor));
  };

  const renderSubLinks = () => {
    return subLinks.map(subLink => {
      return (
        <SubMenu
          title={subLink.title}
          icon={subLink.icon}
          key={subLink.title}
          popupClassName={"test"}
        >
          {subLink.links.map(link => renderLink(link, "#36393f"))}
        </SubMenu>
      );
    });
  };

  return (
    <div className={"sideNav"}>
      <div className="appName">{title}</div>
      <Menu
        theme="dark"
        mode="inline"
        defaultSelectedKeys={[location.pathname]}
        selectedKeys={[location.pathname]}
        style={{ backgroundColor: "#4e535b" }}
      >
        {renderLinks("#4e535b")}
        {renderSubLinks()}
      </Menu>
    </div>
  );
};
export default withRouter(SideNav);
