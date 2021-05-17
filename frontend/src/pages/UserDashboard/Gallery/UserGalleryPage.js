import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "../../../assets/css/layout.css";
import { fetchAlbums } from "../../../reducers/mediaReducer";
import ManageMediaGrid from "../../../components/Gallery/ManageMediaGrid";
import MediaModal from "../../../components/Gallery/MediaModal";

export default () => {
  const { albums, medias } = useSelector(state => state.medias.entities);
  const [activeMedia, setActiveMedia] = useState("");
  const dispatch = useDispatch();
  const { name } = useSelector(state => state.auth.user);

  const handleShowDialog = media => {
    setActiveMedia(media);
  };

  useEffect(() => {
    dispatch(fetchAlbums(`/gallery/myalbums/${name}`));
  }, []);

  useEffect(() => {
    if (activeMedia) {
      setActiveMedia(medias[activeMedia.id]);
    }
  }, [medias]);

  return (
    <div style={{ marginTop: "-30px" }}>
      <h1 className={"bigText centerText whiteColor removeMargin"}>
        My Medias
      </h1>
      <ManageMediaGrid
        handleShowDialog={handleShowDialog}
        albums={Object.values(albums)}
        name={name}
      />
      {activeMedia && (
        <MediaModal
          activeMedia={activeMedia}
          closeModalAction={() => setActiveMedia("")}
          showMediaPreview
          enableManagement
        />
      )}
    </div>
  );
};
