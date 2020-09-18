import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Button, Col, Modal, PageHeader, Row, Tabs } from "antd";
import { Link } from "react-router-dom";
import { displayBytesInReadableForm } from "../../helpers";
import StatisticCard from "../../components/Stats/StatisticCard";
import {
  adminGetUserDetails,
  getUserFileStats,
  updateUserAccountStatus
} from "../../reducers/adminReducer";
import { getUserLinks } from "../../reducers/fileReducer";
import EditUserForm from "../../components/formElements/EditUserForm";
import SharedLinkTable from "../../components/Table/SharedLinkTable";
import { USER_MEDIAS_ENDPOINT } from "../../apis/endpoints";
import MediaGrid from "../../components/Gallery/MediaGrid";
import { BASE_URL } from "../../apis/api";
import Media from "../../components/Gallery/Media";
import { deleteMedia } from "../../reducers/mediaReducer";
import EditMediaForm from "../../components/formElements/EditMediaForm";
import EditUserQuotaForm from "../../components/formElements/EditUserQuotaForm";
import EditUserRole from "../../components/formElements/EditUserRole";
const { TabPane } = Tabs;

export default props => {
  const dispatch = useDispatch();
  const { match } = props;
  const { user } = props.match.params;
  const { users } = useSelector(state => state.admin.entities);
  const account = users[user] || {
    name: "",
    email: "",
    role: "",
    enabled: "",
    quota: { used: 0, max: 25, ignoreQuota: false }
  };
  const { totalLinks, totalViews } = useSelector(
    state => state.admin.userStats
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

  const handleShowDialog = media => {
    setActiveMedia(media);
  };

  useEffect(() => {
    loadUserStats();
  }, []);

  return (
    <div className="extraPadding">
      <PageHeader
        ghost={false}
        onBack={() => window.history.back()}
        title="Back"
        extra={[
          <Button
            key="1"
            type={`${account.enabled ? "danger" : "primary"}`}
            onClick={() =>
              dispatch(updateUserAccountStatus(user, !account.enabled))
            }
          >
            {account.enabled ? "Disable Account" : "Enable Account"}
          </Button>
        ]}
      >
        <Tabs defaultActiveKey="1">
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
            <MediaGrid
              imageEndpoint={`${USER_MEDIAS_ENDPOINT}${user}/image`}
              videoEndpoint={`${USER_MEDIAS_ENDPOINT}${user}/video`}
              handleShowDialog={handleShowDialog}
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
      </PageHeader>
      {activeMedia && (
        <Modal
          title={activeMedia.name}
          visible={activeMedia}
          onCancel={() => setActiveMedia("")}
          footer={null}
          width={550}
        >
          <a
            href={`${BASE_URL}/gallery/${activeMedia.mediaType}/${activeMedia.fileName}`}
          >
            <Media
              media={activeMedia}
              renderVideoControls={true}
              fullSize={true}
            />
          </a>
          <Button
            value="Delete"
            className="centerButton"
            type="danger"
            onClick={() => {
              dispatch(
                deleteMedia(
                  `/gallery/media/delete/${activeMedia.id}`,
                  activeMedia.id
                )
              );
              setActiveMedia("");
            }}
          >
            Delete
          </Button>
          <EditMediaForm media={activeMedia} />
        </Modal>
      )}
    </div>
  );
};
