import React, { useEffect, useState } from "react";
import { Menu } from "antd";
import { NavLink } from "react-router-dom";
import { useSelector } from "react-redux";
import { useLocation } from "react-router-dom";

export default ({ menuDirection }) => {
  const { isAuthenticated, role } = useSelector(state => state.auth);
  const [isAdmin, setIsAdmin] = useState(false);
  let location = useLocation();

  useEffect(() => {
    if (role === "ROLE_ADMIN" && isAuthenticated) {
      setIsAdmin(true);
    } else {
      setIsAdmin(false);
    }
  }, [role, isAuthenticated]);

  return (
    <Menu
      mode={menuDirection}
      defaultSelectedKeys={[location.pathname]}
      selectedKeys={[location.pathname]}
    >
      <Menu.Item key="/">
        <NavLink to="/" className="header item" exact>
          Home
        </NavLink>
      </Menu.Item>
      <Menu.Item key="/images">
        <NavLink to="/images" className="header item">
          Images
        </NavLink>
      </Menu.Item>
      <Menu.Item key="/services">
        <NavLink to="/services" className="header item">
          Services
        </NavLink>
      </Menu.Item>
      {isAuthenticated && (
        <Menu.Item key="/dashboard">
          <NavLink to="/dashboard" exact>
            Dashboard
          </NavLink>
        </Menu.Item>
      )}
      {isAdmin && (
        <Menu.Item key="/admin">
          <NavLink to="/admin" exact>
            Admin
          </NavLink>
        </Menu.Item>
      )}
    </Menu>
  );
};
