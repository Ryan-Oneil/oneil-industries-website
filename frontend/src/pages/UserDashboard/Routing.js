import React from "react";
import ManageServices from "./ManageServices";
import { Route, Switch } from "react-router-dom";
import PrivateRoute from "../../components/PrivateRoute";
import APIPage from "./APIPage";
import ProfilePage from "./Overview";
import UserGalleryPage from "./Gallery/UserGalleryPage";
import UserAlbumsPage from "./Gallery/UserAlbumsPage";
import SideNav from "../../components/site layout/SideNav";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import DatabaseOutlined from "@ant-design/icons/lib/icons/DatabaseOutlined";
import ApiOutlined from "@ant-design/icons/lib/icons/ApiOutlined";
import UploadOutlined from "@ant-design/icons/lib/icons/UploadOutlined";
import PictureOutlined from "@ant-design/icons/lib/icons/PictureOutlined";
import FileImageOutlined from "@ant-design/icons/lib/icons/FileImageOutlined";
import { Layout } from "antd";
import UploadPage from "./Gallery/UploadPage";
import Profile from "../Profile";
import CloudUploadOutlined from "@ant-design/icons/lib/icons/CloudUploadOutlined";
import FolderOutlined from "@ant-design/icons/lib/icons/FolderOutlined";
import ManageFilesPage from "./FileShare/ManageFilesPage";
import EditLinkPage from "./FileShare/EditLinkPage";
import SharePage from "./FileShare/SharePage";
import ShareAltOutlined from "@ant-design/icons/lib/icons/ShareAltOutlined";
const { Content, Sider } = Layout;

export default props => {
  const { match } = props;
  const links = [
    { path: "", icon: <UserOutlined />, name: "Dashboard" },
    { path: "/services", icon: <DatabaseOutlined />, name: "Services" },
    { path: "/api", icon: <ApiOutlined />, name: "Api" }
  ];
  const subLinks = [
    {
      title: "Gallery",
      icon: <PictureOutlined />,
      links: [
        { path: "/gallery/upload", icon: <UploadOutlined />, name: "Upload" },
        {
          path: "/gallery/medias",
          icon: <PictureOutlined />,
          name: "Medias"
        },
        {
          path: "/gallery/albums",
          icon: <FileImageOutlined />,
          name: "Albums"
        }
      ]
    },
    {
      title: "File Share",
      icon: <CloudUploadOutlined />,
      links: [
        {
          path: "/fileshare/share",
          icon: <ShareAltOutlined />,
          name: "Share files"
        },
        {
          path: "/fileshare/files",
          icon: <FolderOutlined />,
          name: "Files"
        }
      ]
    }
  ];

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
        <Content className="extraPadding">
          <Switch>
            <PrivateRoute>
              <Route exact path={match.path} component={ProfilePage} />
              <Route
                exact
                path={`${match.path}/services`}
                component={ManageServices}
              />
              <Route exact path={`${match.path}/api`} component={APIPage} />
              <Route
                exact
                path={`${match.path}/gallery/medias`}
                component={UserGalleryPage}
              />
              <Route
                exact
                path={`${match.path}/gallery/albums`}
                component={UserAlbumsPage}
              />
              <Route
                exact
                path={`${match.path}/gallery/upload`}
                component={UploadPage}
              />
              <Route
                exact
                path={`${match.path}/fileshare/share`}
                component={SharePage}
              />
              <Route
                exact
                path={`${match.path}/fileshare/files`}
                component={ManageFilesPage}
              />
              <Route
                exact
                path={`${match.path}/fileshare/files/edit/:linkID`}
                component={EditLinkPage}
              />
              <Route exact path={`${match.path}/profile`} component={Profile} />
            </PrivateRoute>
          </Switch>
        </Content>
      </Layout>
    </Layout>
  );
};
