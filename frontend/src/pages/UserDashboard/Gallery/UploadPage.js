import React, { useEffect, useState } from "react";
import { Avatar, Button, Card, Col, List, Row } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import Uploader from "../../../components/Uploader";
import UploadForm from "../../../components/formElements/UploadForm";
import { displayBytesInReadableForm } from "../../../helpers";
import { useDispatch, useSelector } from "react-redux";
import { fetchAlbums } from "../../../reducers/mediaReducer";

export default () => {
  const [files, setFiles] = useState([]);
  const { name } = useSelector(state => state.auth.user);
  const { albums } = useSelector(state => state.medias.entities);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchAlbums(`/gallery/myalbums/${name}`));
  }, []);

  const removeFile = fileId => {
    const oldFiles = [...files];

    setFiles(oldFiles.filter(file => file.uid !== fileId));
  };

  return (
    <>
      <Row gutter={[64, 32]} type="flex">
        <Col span={6}>
          <Card>
            <UploadForm
              mediaList={files}
              onSubmitSuccess={() => setFiles([])}
              albums={Object.values(albums)}
            />
          </Card>
        </Col>
        <Col span={18}>
          <Uploader
            showUploadList={false}
            removeFile={removeFile}
            addFile={file => setFiles(prevState => [...prevState, file])}
          />
        </Col>
      </Row>
      <Row gutter={[64, 32]} type="flex">
        <Col span={24}>
          <Card>
            <List
              dataSource={files}
              renderItem={item => (
                <List.Item
                  actions={[
                    <Button
                      danger
                      type="primary"
                      shape="circle"
                      icon={<DeleteOutlined />}
                      onClick={() => {
                        removeFile(item.uid);
                      }}
                    />
                  ]}
                >
                  <List.Item.Meta
                    avatar={
                      <Avatar
                        src={require("../../../assets/images/file.png")}
                      />
                    }
                    title={item.name}
                    description={displayBytesInReadableForm(item.size)}
                  />
                </List.Item>
              )}
            />
          </Card>
        </Col>
      </Row>
    </>
  );
};
