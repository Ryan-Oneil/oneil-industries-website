import MediaGrid from "./MediaGrid";
import React, { useEffect, useState } from "react";
import { USER_MEDIAS_ENDPOINT } from "../../apis/endpoints";
import { useDispatch, useSelector } from "react-redux";
import { Card, Checkbox, Divider, Dropdown, List, Menu } from "antd";
import DeleteOutlined from "@ant-design/icons/lib/icons/DeleteOutlined";
import FolderAddOutlined from "@ant-design/icons/lib/icons/FolderAddOutlined";
import { addMediasToAlbum, deleteMedias } from "../../reducers/mediaReducer";
import Media from "./Media";
import MediaCard from "./MediaCard";
const { SubMenu } = Menu;
const { Meta } = Card;

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
          onClick={() => dispatch(addMediasToAlbum(album.id, selectedMedias))}
        >
          {album.name}
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
          dispatch(deleteMedias(selectedMedias)).then(() =>
            setSelectedMedias([])
          )
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
          mediaItem={item}
          handleShowDialog={handleShowDialog}
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
