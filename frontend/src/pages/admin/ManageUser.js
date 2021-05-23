import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Breadcrumb, Button, Col, message, Row, Tabs } from "antd";
import { displayBytesInReadableForm } from "../../helpers";
import StatisticCard from "../../components/DataDisplay/StatisticCard";
import {
  adminGetUserDetails,
  getUserFileStats,
  updateUserAccountStatus,
} from "../../reducers/adminReducer";
import { getUserLinks } from "../../reducers/fileReducer";
import SharedLinkTable from "../../components/Table/SharedLinkTable";
import EditUserQuotaForm from "../../components/formElements/EditUserQuotaForm";
import EditUserRole from "../../components/formElements/EditUserRole";
import MediaModal from "../../components/Gallery/MediaModal";
import ManageMediaGrid from "../../components/Gallery/ManageMediaGrid";
import { Link } from "react-router-dom";
import { ADMIN_BASE_URL } from "../../constants/constants";
import EditUserForm from "../../components/formElements/EditUserForm";

const { TabPane } = Tabs;

export default (props) => {
  const dispatch = useDispatch();
  const { match } = props;
  const { user } = props.match.params;
  const { users } = useSelector((state) => state.admin.entities);
  const account = users[user] || {
    name: "",
    email: "",
    role: "",
    enabled: "",
    quota: { used: 0, max: 25, ignoreQuota: false },
  };
  const { totalLinks, totalViews } = useSelector(
    (state) => state.admin.userStats
  );
  const [loadingFileStats, setLoadingFileStats] = useState(false);
  const [loadingUserDetails, setLoadingUserDetails] = useState(true);
  const [activeMedia, setActiveMedia] = useState("");

  const loadUserStats = () => {
    dispatch(getUserFileStats(user)).then(() => setLoadingFileStats(false));
    dispatch(adminGetUserDetails(user)).then(() =>
      setLoadingUserDetails(false)
    );
  };

  const handleShowDialog = (media) => {
    setActiveMedia(media);
  };

  useEffect(() => {
    loadUserStats();
  }, []);

  return (
    <div className="extraPadding">
      <Breadcrumb>
        <Breadcrumb.Item>
          <Link to={ADMIN_BASE_URL}>Dashboard</Link>
        </Breadcrumb.Item>
        <Breadcrumb.Item>
          <Link to={`${ADMIN_BASE_URL}/users`}>Users</Link>
        </Breadcrumb.Item>
        <Breadcrumb.Item>{user}</Breadcrumb.Item>
      </Breadcrumb>
      <Tabs
        defaultActiveKey="1"
        size={"large"}
        className={"userManagementNav"}
        tabBarExtraContent={
          <Button
            key="1"
            type={`${account.enabled ? "danger" : "primary"}`}
            onClick={() =>
              dispatch(
                updateUserAccountStatus(user, !account.enabled)
              ).then(() =>
                message.success(
                  `User account has been ${
                    !account.enabled ? "enabled" : "disabled"
                  }`
                )
              )
            }
          >
            {account.enabled ? "Disable Account" : "Enable Account"}
          </Button>
        }
      >
        <TabPane tab="Account Settings" key="1">
          <Row gutter={[32, 32]} type="flex">
            <Col xs={24} sm={12} md={12} lg={12} xl={8}>
              <EditUserForm user={account} loading={loadingUserDetails} />
            </Col>
            <Col xs={24} sm={12} md={12} lg={12} xl={8}>
              <EditUserQuotaForm username={user} quota={account.quota} />
            </Col>
            <Col xs={24} sm={12} md={12} lg={12} xl={8}>
              <EditUserRole role={account.role} username={user} />
            </Col>
          </Row>
        </TabPane>
        <TabPane tab="Files" key="2">
          <SharedLinkTable
            editPath={match.path}
            fetchData={(page, size, sorter) =>
              getUserLinks(user, page, size, sorter)
            }
          />
        </TabPane>
        <TabPane tab="Medias" key="3">
          <ManageMediaGrid
            handleShowDialog={handleShowDialog}
            albums={[]}
            name={user}
          />
        </TabPane>
        <TabPane tab="Stats" key="4">
          <Row gutter={[32, 32]} type="flex" justify="center">
            <Col xs={24} sm={24} md={5} lg={5} xl={5}>
              <StatisticCard
                title={"Total Links"}
                value={totalLinks}
                loading={loadingFileStats}
              />
            </Col>
            <Col xs={24} sm={24} md={5} lg={5} xl={5}>
              <StatisticCard
                title={"Total Link Views"}
                value={totalViews}
                loading={loadingFileStats}
              />
            </Col>
            <Col xs={24} sm={24} md={5} lg={5} xl={5}>
              <StatisticCard
                title={"Total Used Storage"}
                value={displayBytesInReadableForm(account.quota.used)}
                loading={loadingUserDetails}
              />
            </Col>
            <Col xs={24} sm={24} md={5} lg={5} xl={5}>
              <StatisticCard title={"Total Media"} value={0} />
            </Col>
          </Row>
        </TabPane>
      </Tabs>
      {/*</PageHeader>*/}
      {activeMedia && (
        <MediaModal
          activeMedia={activeMedia}
          closeModalAction={() => setActiveMedia("")}
          showMediaPreview
          enableManagement
        />
      )}
    </div>
  );
};
