import React from 'react';
import {connect} from "react-redux";
import _ from 'lodash';

import {deleteMedia, fetchAlbums, fetchUserImages} from "../../../actions";
import Media from "./Elements/Media";
import '../../../assets/css/layout.css';
import Modal from "./Elements/Modal";
import EditMediaForm from "../../formElements/EditMediaForm";
import RenderMedias from "./Elements/RenderMedias";

class UserGalleryPage extends React.Component {

    state = { isModalOpen: false };

    handleShowDialog = (media) => {
        this.setState({ isOpen: !this.state.isOpen, media });
    };

    componentDidMount() {
        if (!this.props.medias.albums) {
            this.props.medias.albums = this.props.fetchAlbum(`/gallery/myalbums/${this.props.user}`);
        }
        if (!this.props.medias.userMediasList) {
            this.props.fetchUserImages(`/gallery/medias/user/${this.props.user}`);
        }
    }

    returnAlbumName = (media) => {
        let album = _.find(this.props.medias.albums, (mediaAlbum) => { return mediaAlbum.album.id === media.albumID });
        return album ? album.album.name : "none";
    };

    render() {
        return (
            <div>
                <h1 className="ui center aligned header">
                    {this.props.user}'s Gallery
                </h1>
                <div className="ui three column grid">
                    {RenderMedias(this.props.medias, this.handleShowDialog)}
                    {this.state.isOpen && (<Modal
                        title={this.state.media.name}
                        closeModal = {() => this.handleShowDialog()}
                    >
                        <div className="image">
                            <a href={`http://localhost:8080/api/gallery/media/${this.state.media.id}`}>
                                <Media media={this.state.media} renderVideoControls={true}/>
                            </a>
                        </div>
                        <button value="Delete" className="centerButton ui negative button center aligned"
                                onClick={() => this.props.deleteMedia(`/gallery/media/delete/${this.state.media.id}`, this.state.media.id)}>
                            Delete
                        </button>
                        <EditMediaForm
                            media={this.state.media}
                            initialValues={{'name': this.state.media.name,
                                'linkStatus' : this.state.media.linkStatus,
                                'album': this.returnAlbumName(this.state.media)}}
                        />

                        {this.props.medias.deleteError && <div className="ui error message">
                            {<div className="header">{this.props.medias.deleteError}</div>}
                        </div>}
                    </Modal>)}
                </div>
            </div>
        );
    }
}
const mapStateToProps = (state) => {
    return {medias: state.medias};
};

export default connect(
    mapStateToProps,
    { fetchUserImages, deleteMedia, fetchAlbum: fetchAlbums }
)(UserGalleryPage);