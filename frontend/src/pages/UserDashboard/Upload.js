import React, { useCallback, useState, useEffect } from "react";
import DropArea from "../../components/Gallery/DropArea";
import UploadCard from "../../components/Gallery/UploadCard";
import { connect } from "react-redux";
import UploadForm from "../../components/formElements/UploadForm";
import { getUserStorage } from "../../actions/profile";

const Upload = props => {
  useEffect(() => {
    props.getUserStorage("/user/profile/storageQuota");
  }, []);

  const [medias, setMedias] = useState([]);
  const { profile } = props;
  const [usedStorage, setUsedStorage] = useState(0);
  const [reachedUploadLimit, setReachedUploadLimit] = useState(false);

  const setStorageLimit = useCallback(
    limit => {
      const { storageQuota } = profile;
      const max = storageQuota.max * 1000000000;
      setUsedStorage(limit);

      if (
        !storageQuota.ignoreQuota &&
        max - (usedStorage + storageQuota.used) < 0
      ) {
        setReachedUploadLimit(true);
      } else {
        setReachedUploadLimit(false);
      }
    },
    [profile, usedStorage]
  );

  const onDrop = useCallback(
    acceptedFiles => {
      if (!reachedUploadLimit) {
        acceptedFiles.map(file => {
          setStorageLimit(usedStorage + file.size);
          const reader = new FileReader();

          reader.onload = e => {
            setMedias(prevState => [
              ...prevState,
              {
                name: file.name,
                src: e.target.result,
                file,
                fileExt: file.name.substr(file.name.indexOf(".")),
                reachedUploadLimit: reachedUploadLimit
              }
            ]);
          };
          reader.readAsDataURL(file);
          return file;
        });
      }
    },
    [reachedUploadLimit, usedStorage, setStorageLimit]
  );

  const imageList = () => {
    return medias.map(media => {
      const onDelete = id => {
        setMedias(medias.filter(mediaItem => mediaItem.id !== id));
        setStorageLimit(usedStorage - media.file.size);
      };

      const onChange = e => {
        e.target.value = e.target.value.replace(/[^\w\s]/gi, "");
        media.name = e.target.value + media.fileExt;
      };

      return (
        <div className="four wide column" key={media.id}>
          <UploadCard
            media={{
              id: media.id,
              src: media.src,
              mediaType: media.file.type.substr(0, media.file.type.indexOf("/"))
            }}
            value={media.file.name.substr(0, media.file.name.indexOf("."))}
            onChange={onChange}
            onDelete={() => onDelete(media.id)}
            reachedUploadLimit={media.reachedUploadLimit}
          />
        </div>
      );
    });
  };

  return (
    <>
      <div className="ui equal width centered grid">
        <div className="row">
          <div className="ui container bottomMargin">
            <DropArea onDrop={onDrop} />
          </div>
        </div>
        <div className="centered row">
          <UploadForm
            mediasList={medias}
            reachedUploadLimit={reachedUploadLimit}
            onSubmitSuccess={() => setMedias([])}
          />
        </div>
        {imageList()}
      </div>
    </>
  );
};
const mapStateToProps = state => ({
  profile: state.profile
});

export default connect(mapStateToProps, {
  getUserStorage
})(Upload);
