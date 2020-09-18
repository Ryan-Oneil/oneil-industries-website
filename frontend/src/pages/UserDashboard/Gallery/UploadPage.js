import React, { useState } from "react";
import { Avatar, Button, Card, Col, List, Row } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import Uploader from "../../../components/Uploader";
import UploadForm from "../../../components/formElements/UploadForm";
import { displayBytesInReadableForm } from "../../../helpers";

export default () => {
  const [files, setFiles] = useState([]);

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
