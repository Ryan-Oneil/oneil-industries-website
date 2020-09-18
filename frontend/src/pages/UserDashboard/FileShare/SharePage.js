import React, { useEffect, useState } from "react";
import { Avatar, Button, Card, Col, List, Row } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import Uploader from "../../../components/Uploader";
import StatisticCard from "../../../components/Stats/StatisticCard";
import { displayBytesInReadableForm } from "../../../helpers";
import ShareLinkForm from "../../../components/formElements/ShareLinkForm";

export default () => {
  const [files, setFiles] = useState([]);
  const [size, setSize] = useState(0);

  const removeFile = fileId => {
    const oldFiles = [...files];

    setFiles(oldFiles.filter(file => file.uid !== fileId));
  };

  useEffect(() => {
    let size = 0;

    files.forEach(file => (size += file.size));
    setSize(size);
  }, [files]);

  return (
    <>
      <Row gutter={[32, 32]} type="flex">
        <Col span={8}>
          <ShareLinkForm resetFiles={() => setFiles([])} files={files} />
        </Col>
        <Col span={16}>
          <Uploader
            showUploadList={false}
            removeFile={removeFile}
            addFile={file => setFiles(prevState => [...prevState, file])}
          />
        </Col>
      </Row>
      <Row gutter={[32, 32]} type="flex">
        <Col span={8}>
          <StatisticCard
            title={"Total Size"}
            value={displayBytesInReadableForm(size)}
          />
        </Col>
        <Col span={16}>
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
