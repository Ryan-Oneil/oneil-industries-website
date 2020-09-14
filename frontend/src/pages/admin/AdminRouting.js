import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { Route, Switch } from "react-router-dom";
import Users from "./Users";
import ManageUser from "./ManageUser";
import PrivateRoute from "../../components/PrivateRoute";
import Media from "./Media/MediaAdmin";
import Approval from "./Media/Approval";
import Stats from "./Stats";
import SideNav from "../../components/site layout/SideNav";
import { Layout } from "antd";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import DatabaseOutlined from "@ant-design/icons/lib/icons/DatabaseOutlined";
import PictureOutlined from "@ant-design/icons/lib/icons/PictureOutlined";
import FileImageOutlined from "@ant-design/icons/lib/icons/FileImageOutlined";
const { Content, Sider } = Layout;

export default props => {
  const { isAuthenticated, role } = useSelector(state => state.auth);
  const { match } = props;

  const links = [
    { path: "", icon: <DatabaseOutlined />, name: "Stats" },
    { path: "/users", icon: <UserOutlined />, name: "Users" }
  ];
  const subLinks = [
    {
      title: "Gallery",
      icon: <PictureOutlined />,
      links: [
        { path: "/gallery/media", icon: <PictureOutlined />, name: "Medias" },
        {
          path: "/gallery/approval",
          icon: <FileImageOutlined />,
          name: "Approvals"
        }
      ]
    }
  ];

  useEffect(() => {
    if (!isAuthenticated || role !== "ROLE_ADMIN") {
      props.history.push("/");
    }
  }, []);

  return (
    <Layout>
      <Sider breakpoint="lg" collapsedWidth="0">
        <SideNav
          path={match.path}
          links={links}
          title={"Dashboard"}
          subLinks={subLinks}
        />
      </Sider>
      <Layout className={"blueBackgroundColor"}>
        <Content>
          <Switch>
            <PrivateRoute>
              <Route exact path={match.path} component={Stats} />
              <Route exact path={`${match.path}/users`} component={Users} />
              <Route
                path={`${match.path}/users/:user`}
                component={ManageUser}
              />
              <Route
                exact
                path={`${match.path}/gallery/media`}
                component={Media}
              />
              <Route
                exact
                path={`${match.path}/gallery/approval`}
                component={Approval}
              />
            </PrivateRoute>
          </Switch>
        </Content>
      </Layout>
    </Layout>
  );
};
