import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import MediaModal from "../../../components/Gallery/MediaModal";
import ManageMediaGrid from "../../../components/Gallery/ManageMediaGrid";

export default () => {
  const { medias } = useSelector(state => state.medias.entities);
  const [activeMedia, setActiveMedia] = useState("");

  const handleShowDialog = media => {
    setActiveMedia(media);
  };

  useEffect(() => {
    if (activeMedia) {
      setActiveMedia(medias[activeMedia.id]);
    }
  }, [medias]);

  return (
    <>
      <h1 className={"bigText centerText whiteColor"}>User's Medias</h1>
      <ManageMediaGrid
        handleShowDialog={handleShowDialog}
        albums={[]}
        showUploader
      />
      {activeMedia && (
        <MediaModal
          activeMedia={activeMedia}
          closeModalAction={() => setActiveMedia("")}
          showMediaPreview
          enableManagement
        />
      )}
    </>
  );
};
