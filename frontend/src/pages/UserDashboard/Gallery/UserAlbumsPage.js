import React, { useEffect, useState } from "react";
import Media from "../../../components/Gallery/Media";
import { useDispatch, useSelector } from "react-redux";
import { deleteAlbum, fetchAlbums } from "../../../reducers/mediaReducer";
import {
  Button,
  Card,
  Col,
  Empty,
  Image,
  message,
  Modal,
  Row,
  Space
} from "antd";
import EditAlbumForm from "../../../components/formElements/EditAlbumForm";
import MediaCard from "../../../components/Gallery/MediaCard";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import EyeOutlined from "@ant-design/icons/lib/icons/EyeOutlined";

export default () => {
  const [activeAlbum, setActiveAlbum] = useState("");
  const [loading, setLoading] = useState(true);
  const { name } = useSelector(state => state.auth.user);
  const { albums, medias } = useSelector(state => state.medias.entities);
  const dispatch = useDispatch();

  const handleShowDialog = album => {
    setActiveAlbum(album);
  };

  useEffect(() => {
    dispatch(fetchAlbums(`/gallery/myalbums/${name}`)).then(() =>
      setLoading(false)
    );
  }, []);

  const renderList = () => {
    return Object.values(albums).map(album => {
      const { fileName = "", mediaType = "" } = medias[album.medias[0]] || {
        fileName: "",
        mediaType: ""
      };
      return (
        <Col key={album.id} xs={18} sm={6} md={6} lg={7} xl={4}>
          <MediaCard
            handleShowDialog={handleShowDialog.bind(this, album)}
            title={album.name || album.id}
            mediaType={mediaType}
            mediaFileName={fileName}
          />
        </Col>
      );
    });
  };

  return (
    <>
      <h1
        className={"bigText centerText whiteColor"}
        style={{ marginTop: "-30px" }}
      >
        My Albums
      </h1>
      {Object.values(albums).length === 0 && (
        <Card>
          <Empty description={"You don't have any albums"} />
        </Card>
      )}
      <Row
        gutter={[32, 32]}
        style={{
          height: "84vh",
          overflow: "auto"
        }}
      >
        {renderList()}
      </Row>
      {activeAlbum && (
        <Modal
          title={activeAlbum.name}
          visible={true}
          onCancel={() => setActiveAlbum("")}
          footer={null}
        >
          {activeAlbum.medias.length > 0 && (
            <Media
              fileName={medias[activeAlbum.medias[0]].fileName}
              mediaType={medias[activeAlbum.medias[0]].mediaType}
            />
          )}
          {activeAlbum.medias.length === 0 && (
            <Image
              alt={"No media"}
              src={require("../../../assets/images/noimage.png")}
              preview={false}
              style={{ margin: "auto" }}
              width={"100%"}
            />
          )}
          <div className={"centerFlexContent"}>
            <Space>
              <Button
                className="formattedBackground"
                type="primary"
                icon={<EyeOutlined />}
                onClick={() =>
                  window.open(`/gallery/album/${activeAlbum.id}`, "_blank")
                }
              >
                View
              </Button>
              <Button
                type="danger"
                icon={<DeleteOutlined />}
                onClick={() => {
                  dispatch(deleteAlbum(activeAlbum.id)).then(() => {
                    message.success("Successfully delete album");
                    setActiveAlbum("");
                  });
                }}
              >
                Delete
              </Button>
            </Space>
          </div>

          <EditAlbumForm album={activeAlbum} />
        </Modal>
      )}
      {albums.length === 0 && (
        <Card>
          <Empty
            description={loading ? "Fetching Albums" : "No Albums found"}
          />
        </Card>
      )}
    </>
  );
};
