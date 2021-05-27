import MediaGrid from "./MediaGrid";
import React, { useState } from "react";
import { USER_MEDIAS_ENDPOINT } from "../../apis/endpoints";
import { useDispatch } from "react-redux";
import { Dropdown, List, Menu, message } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import FolderAddOutlined from "@ant-design/icons/lib/icons/FolderAddOutlined";
import { addMediasToAlbum, deleteMedias } from "../../reducers/mediaReducer";
import ManageMediaCard from "./ManageMediaCard";
import SettingOutlined from "@ant-design/icons/lib/icons/SettingOutlined";

const { SubMenu } = Menu;

export default ({
  handleShowDialog,
  albums = [],
  showUploader,
  name,
  endpoint,
}) => {
  const [enableManage, setEnableManage] = useState(false);
  const [selectedMedias, setSelectedMedias] = useState([]);
  const disableOptions = !enableManage || !selectedMedias.length > 0;
  const dispatch = useDispatch();

  const toggleManagedMedia = (mediaId) => {
    if (!selectedMedias.includes(mediaId)) {
      setSelectedMedias((prevState) => [...prevState, mediaId]);
    } else {
      const filteredArray = selectedMedias.filter((id) => id !== mediaId);
      setSelectedMedias(filteredArray);
    }
  };

  const renderAlbums = () => {
    return albums.map((album) => {
      return (
        <Menu.Item
          key={album.id}
          onClick={() =>
            dispatch(addMediasToAlbum(album.id, selectedMedias)).then(() => {
              setSelectedMedias([]);
              message.success("Successfully added selected medias to Album");
            })
          }
        >
          {album.name || album.id}
        </Menu.Item>
      );
    });
  };

  const options = (
    <Menu>
      <SubMenu
        key="1"
        icon={<FolderAddOutlined />}
        disabled={disableOptions}
        title={"Add to Album"}
      >
        <Menu style={{ maxHeight: "60vh", overflow: "auto" }}>
          {renderAlbums()}
        </Menu>
      </SubMenu>
      <Menu.Item
        key="2"
        icon={<DeleteOutlined />}
        disabled={disableOptions}
        onClick={() =>
          dispatch(deleteMedias(selectedMedias)).then(() => {
            setSelectedMedias([]);
            message.success("Successfully deleted selected medias");
          })
        }
      >
        Delete
      </Menu.Item>
    </Menu>
  );

  const cardRender = (item) => {
    return (
      <List.Item key={item.id}>
        <ManageMediaCard
          mediaFileName={item.fileName}
          title={showUploader ? item.uploader : item.name}
          mediaType={item.mediaType}
          dateAdded={item.dateAdded}
          handleShowDialog={handleShowDialog.bind(this, item)}
          manageEnabled={enableManage}
          onSelect={() => toggleManagedMedia(item.id)}
          isSelected={selectedMedias.includes(item.id)}
        />
      </List.Item>
    );
  };

  return (
    <>
      <Dropdown.Button
        className={`bulkMediaManage ${
          enableManage ? "bulkMediaManageSelected" : ""
        }`}
        size={"large"}
        onClick={() => {
          setEnableManage((prevState) => !prevState);
        }}
        overlay={options}
        style={{ float: "right", marginBottom: "1%", marginRight: 35 }}
      >
        <SettingOutlined />
        Bulk Manage
      </Dropdown.Button>
      <MediaGrid
        height={"82vh"}
        mediaEndpoint={endpoint ? endpoint : `${USER_MEDIAS_ENDPOINT}${name}`}
        mediaCardLayout={cardRender}
        style={{ width: "100%" }}
      />
    </>
  );
};
