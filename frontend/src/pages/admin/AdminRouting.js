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
import CloudUploadOutlined from "@ant-design/icons/lib/icons/CloudUploadOutlined";
import FolderOutlined from "@ant-design/icons/lib/icons/FolderOutlined";
import ViewAllLinks from "./fileshare/ViewAllLinks";
import EditLinkPage from "../UserDashboard/FileShare/EditLinkPage";
import { HOME_URL } from "../../constants/constants";
const { Content, Sider } = Layout;

export default (props) => {
  const { isAuthenticated, role } = useSelector((state) => state.auth);
  const { match } = props;

  const links = [
    { path: "", icon: <DatabaseOutlined />, name: "Stats" },
    { path: "/users", icon: <UserOutlined />, name: "Users" },
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
          name: "Approvals",
        },
      ],
    },
    {
      title: "File Share",
      icon: <CloudUploadOutlined />,
      links: [
        {
          path: "/fileshare/files",
          icon: <FolderOutlined />,
          name: "Files",
        },
      ],
    },
  ];

  useEffect(() => {
    if (!isAuthenticated || role !== "ROLE_ADMIN") {
      props.history.push(HOME_URL);
    }
  }, []);

  return (
    <Layout>
      <Sider breakpoint="lg" collapsedWidth="0">
        <SideNav
          path={match.path}
          links={links}
          title={"Admin Dashboard"}
          subLinks={subLinks}
        />
      </Sider>
      <Layout
        className={"mainBackgroundColor dashboard"}
        style={{ overflow: "auto" }}
      >
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
              <Route
                exact
                path={`${match.path}/fileshare/files`}
                component={ViewAllLinks}
              />
              <Route
                exact
                path={`${match.path}/fileshare/files/edit/:linkID`}
                component={EditLinkPage}
              />
            </PrivateRoute>
          </Switch>
        </Content>
      </Layout>
    </Layout>
  );
};
