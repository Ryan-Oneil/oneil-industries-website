import React from "react";
import { Menu } from "antd";
import { NavLink } from "react-router-dom";
import LoginOutlined from "@ant-design/icons/lib/icons/LoginOutlined";
import UserDropDown from "./UserDropDown";
import { useSelector } from "react-redux";

export default ({ menuDirection }) => {
  const { isAuthenticated } = useSelector(state => state.auth);

  return (
    <Menu mode={menuDirection}>
      {!isAuthenticated && (
        <Menu.Item key="/login">
          <NavLink to="/login" exact>
            <LoginOutlined />
            Login
          </NavLink>
        </Menu.Item>
      )}
      {isAuthenticated && <UserDropDown />}
    </Menu>
  );
};
