import MediaGrid from "./MediaGrid";
import React, { useState } from "react";
import { USER_MEDIAS_ENDPOINT } from "../../apis/endpoints";
import { useDispatch, useSelector } from "react-redux";
import { Checkbox, Dropdown, List, Menu, message } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import FolderAddOutlined from "@ant-design/icons/lib/icons/FolderAddOutlined";
import { addMediasToAlbum, deleteMedias } from "../../reducers/mediaReducer";
import MediaCard from "./MediaCard";

const { SubMenu } = Menu;

export default ({ handleShowDialog, albums = [] }) => {
  const { name } = useSelector(state => state.auth.user);
  const [enableManage, setEnableManage] = useState(false);
  const [selectedMedias, setSelectedMedias] = useState([]);
  const disableOptions = !enableManage || !selectedMedias.length > 0;
  const dispatch = useDispatch();

  const toggleManagedMedia = mediaId => {
    if (!selectedMedias.includes(mediaId)) {
      setSelectedMedias(prevState => [...prevState, mediaId]);
    } else {
      const filteredArray = selectedMedias.filter(id => id !== mediaId);
      setSelectedMedias(filteredArray);
    }
  };

  const checkBox = mediaId => {
    if (!enableManage) {
      return null;
    }
    return <Checkbox onChange={() => toggleManagedMedia(mediaId)} />;
  };

  const renderAlbums = () => {
    return albums.map(album => {
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
        <Menu>{renderAlbums()}</Menu>
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

  const cardRender = item => {
    return (
      <List.Item key={item.id}>
        <MediaCard
          mediaFileName={item.fileName}
          title={item.name}
          mediaType={item.mediaType}
          dateAdded={item.dateAdded}
          handleShowDialog={handleShowDialog.bind(this, item)}
          cardExtras={checkBox(item.id)}
        />
      </List.Item>
    );
  };

  return (
    <MediaGrid
      imageEndpoint={`${USER_MEDIAS_ENDPOINT}${name}/image`}
      videoEndpoint={`${USER_MEDIAS_ENDPOINT}${name}/video`}
      tabExtraActions={
        <Dropdown.Button
          className={"bulkMediaManage"}
          size={"large"}
          onClick={() => {
            setEnableManage(prevState => !prevState);
            setSelectedMedias([]);
          }}
          overlay={options}
        >
          Bulk Manage
        </Dropdown.Button>
      }
      mediaCardLayout={cardRender}
    />
  );
};
