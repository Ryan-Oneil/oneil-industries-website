import React from "react";
import { Menu } from "antd";
import { NavLink, withRouter } from "react-router-dom";
import SubMenu from "antd/es/menu/SubMenu";

const SideNav = ({ path, location, links = [], title, subLinks = [] }) => {
  const renderLink = link => {
    return (
      <Menu.Item key={`${path}${link.path}`}>
        <NavLink to={`${path}${link.path}`} exact>
          {link.icon}
          {link.name}
        </NavLink>
      </Menu.Item>
    );
  };

  const renderLinks = () => {
    return links.map(link => renderLink(link));
  };

  const renderSubLinks = () => {
    return subLinks.map(subLink => {
      return (
        <SubMenu title={subLink.title} icon={subLink.icon} key={subLink.title}>
          {subLink.links.map(link => renderLink(link))}
        </SubMenu>
      );
    });
  };

  return (
    <div>
      <div className="appName">{title}</div>
      <Menu
        theme="dark"
        mode="inline"
        defaultSelectedKeys={[location.pathname]}
        selectedKeys={[location.pathname]}
      >
        {renderLinks()}
        {renderSubLinks()}
      </Menu>
    </div>
  );
};
export default withRouter(SideNav);
