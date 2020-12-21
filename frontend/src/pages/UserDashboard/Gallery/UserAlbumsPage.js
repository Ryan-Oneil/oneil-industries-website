import React, { useEffect, useState } from "react";
import Media from "../../../components/Gallery/Media";
import { useDispatch, useSelector } from "react-redux";
import { deleteAlbum, fetchAlbums } from "../../../reducers/mediaReducer";
import { Button, Card, Col, Divider, Empty, Modal, Row } from "antd";
import { DELETE_ALBUM } from "../../../apis/endpoints";
import { setError } from "../../../reducers/globalErrorReducer";
import { getApiError } from "../../../helpers";
const { Meta } = Card;

export default () => {
  const [album, setAlbum] = useState("");
  const [albums, setAlbums] = useState([]);
  const [loading, setLoading] = useState(true);
  const { name } = useSelector(state => state.auth.user);
  const dispatch = useDispatch();

  const handleShowDialog = album => {
    setAlbum(album);
  };

  useEffect(() => {
    fetchAlbums(`/gallery/myalbums/${name}`)
      .then(data => setAlbums(data))
      .catch(error => dispatch(setError(getApiError(error))))
      .finally(() => setLoading(false));
  }, []);

  const renderList = () => {
    return albums.map(album => {
      return (
        <Col key={album.id} xs={18} sm={6} md={6} lg={7} xl={4}>
          <Card>
            <div className="pointerCursor">
              <Media
                media={album.medias[0]}
                onClick={handleShowDialog.bind(this, album)}
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
      <Row gutter={[32, 32]}>{renderList()}</Row>
      {album && (
        <Modal
          title={album.name}
          visible={album}
          onCancel={() => setAlbum("")}
          footer={null}
        >
          <a href={`/gallery/album/${album.id}`}>
            <Media media={album.medias[0]} renderVideoControls={true} />
          </a>
          <Button
            value="Delete"
            className="centerButton"
            type="danger"
            onClick={() => {
              dispatch(deleteAlbum(DELETE_ALBUM, album.id));
              setAlbum("");
            }}
          >
            Delete
          </Button>
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
