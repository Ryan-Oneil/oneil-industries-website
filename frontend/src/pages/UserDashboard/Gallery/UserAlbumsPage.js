import React, { useEffect, useState } from "react";
import Media from "../../../components/Gallery/Media";
import { useDispatch, useSelector } from "react-redux";
import { deleteAlbum, fetchAlbums } from "../../../reducers/mediaReducer";
import { Button, Card, Col, Divider, Empty, Image, Modal, Row } from "antd";
import { ALBUM_DELETE } from "../../../apis/endpoints";
import EditAlbumForm from "../../../components/formElements/EditAlbumForm";
const { Meta } = Card;

export default () => {
  const [albumId, setAlbumId] = useState("");
  const [loading, setLoading] = useState(true);
  const { name } = useSelector(state => state.auth.user);
  const { albums, medias } = useSelector(state => state.medias.entities);
  const dispatch = useDispatch();

  const handleShowDialog = albumId => {
    setAlbumId(albumId);
  };

  useEffect(() => {
    dispatch(fetchAlbums(`/gallery/myalbums/${name}`)).then(() =>
      setLoading(false)
    );
  }, []);

  const renderList = () => {
    return Object.values(albums).map(album => {
      return (
        <Col key={album.id} xs={18} sm={6} md={6} lg={7} xl={4}>
          <Card>
            <div className="pointerCursor">
              <Media
                media={medias[album.medias[0]]}
                onClick={handleShowDialog.bind(this, album.id)}
              />
            </div>
            <Divider />
            <Meta title={album.name} style={{ textAlign: "center" }} />
          </Card>
        </Col>
      );
    });
  };

  return (
    <div
      className="marginPadding"
      style={{
        paddingLeft: "30px",
        marginTop: "-10px"
      }}
    >
      {Object.values(albums).length === 0 && (
        <Card>
          <Empty description={"You don't have any albums"} />
        </Card>
      )}
      <Row gutter={[32, 32]}>{renderList()}</Row>
      {albumId && (
        <Modal
          title={albums[albumId].name}
          visible={albumId}
          onCancel={() => setAlbumId("")}
          footer={null}
        >
          <a href={`/gallery/album/${albumId}`}>
            <Media
              media={medias[albums[albumId].medias[0]]}
              renderVideoControls={true}
            />
          </a>
          <Button
            value="Delete"
            className="centerButton"
            type="danger"
            onClick={() => {
              dispatch(deleteAlbum(ALBUM_DELETE, albumId));
              setAlbumId("");
            }}
          >
            Delete
          </Button>
          <EditAlbumForm album={albums[albumId]} />
        </Modal>
      )}
      {albums.length === 0 && (
        <Card>
          <Empty
            description={loading ? "Fetching Albums" : "No Albums found"}
          />
        </Card>
      )}
    </div>
  );
};
