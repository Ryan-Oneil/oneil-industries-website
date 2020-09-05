import React from "react";
import ManageServices from "./ManageServices";
import SideBarNav from "../../components/site layout/SideBarNav";
import { NavLink, Route, Switch } from "react-router-dom";
import PrivateRoute from "../../components/PrivateRoute";
import APIPage from "./APIPage";
import ProfilePage from "./Overview";
import SubNavMenu from "../../components/site layout/SubNavMenu";
import UserGalleryPage from "./UserGalleryPage";
import UserAlbumsPage from "./UserAlbumsPage";
import { connect } from "react-redux";
import Upload from "./Upload";
import SideNav from "../../components/site layout/SideNav";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
import DatabaseOutlined from "@ant-design/icons/lib/icons/DatabaseOutlined";
import ApiOutlined from "@ant-design/icons/lib/icons/ApiOutlined";
import UploadOutlined from "@ant-design/icons/lib/icons/UploadOutlined";
import PictureOutlined from "@ant-design/icons/lib/icons/PictureOutlined";
import FileImageOutlined from "@ant-design/icons/lib/icons/FileImageOutlined";
import { Layout } from "antd";
const { Header, Content, Sider } = Layout;

class Routing extends React.Component {
  render() {
    const { match } = this.props;
    const { user } = this.props.auth;
    const links = [
      { path: "", icon: <UserOutlined />, name: "Account" },
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
      }
    ];

    return (
      <Layout style={{ height: "80vh" }}>
        <Sider breakpoint="lg" collapsedWidth="0" className={"dashboard"}>
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
                  render={props => <UserGalleryPage {...props} user={user} />}
                />
                <Route
                  exact
                  path={`${match.path}/gallery/albums`}
                  render={props => <UserAlbumsPage {...props} user={user} />}
                />
                <Route
                  exact
                  path={`${match.path}/gallery/upload`}
                  component={Upload}
                />
              </PrivateRoute>
            </Switch>
          </Content>
        </Layout>
      </Layout>
    );
  }
}
const mapStateToProps = state => {
  return { auth: state.auth };
};
export default connect(mapStateToProps)(Routing);
