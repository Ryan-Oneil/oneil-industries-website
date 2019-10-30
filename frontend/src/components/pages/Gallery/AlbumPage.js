import React from 'react';
import Media from "./Elements/Media";
import RenderMedias from "./Elements/RenderMedias";
import Modal from "./Elements/Modal";
import {apiGetCall} from "../../../apis/api";

class AlbumPage extends React.Component {

    state = { isModalOpen: false, album : null, message: null };

    handleShowDialog = (media) => {
        this.setState({ isOpen: !this.state.isOpen, media });
    };

    componentDidMount() {
        const { match: { params } } = this.props;
        // Don't really need redux in this single component so manually getting and storing in local state
        apiGetCall(`/gallery/album/${params.albumName}`).then(response => {
            this.setState({album: response.data});
        }).catch(error => {
            this.setState({message: error.message});
            }
        );
    }

    render() {
        const { match: { params } } = this.props;
        return (
            <div>
                <h1 className="ui center aligned header">
                    Album : {params.albumName}
                </h1>
                <div className="ui three column grid">
                    {RenderMedias(this.state.album, this.state.message, this.handleShowDialog)}
                </div>
                {this.state.isOpen && (<Modal
                    title={this.state.media.name}
                    closeModal = {() => this.handleShowDialog()}
                >
                    <div className="image">
                        <a href={`http://localhost:8080/api/gallery/media/${this.state.media.id}`}>
                            <Media media={this.state.media} renderVideoControls={true}/>
                        </a>
                    </div>
                    <div className="centerText">
                        <p>Uploader: {this.state.media.uploader}</p>
                        <p>Uploaded: {this.state.media.dateAdded}</p>
                    </div>
                </Modal>)}
            </div>
        );
    }
}
export default AlbumPage;