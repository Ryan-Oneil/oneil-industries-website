import React, { useState } from "react";
import { Button, Drawer } from "antd";
import MainNav from "./MainNav";
import MenuOutlined from "@ant-design/icons/lib/icons/MenuOutlined";
import AuthNav from "./AuthNav";

export default () => {
  const [showMenu, setShowMenu] = useState(false);

  return (
    <>
      <Button
        icon={<MenuOutlined />}
        className="responsiveMenuButton"
        onClick={() => setShowMenu(true)}
      />

      <Drawer
        placement="right"
        closable={false}
        onClose={() => setShowMenu(false)}
        visible={showMenu}
        footer={<AuthNav />}
      >
        <MainNav />
      </Drawer>
    </>
  );
};
