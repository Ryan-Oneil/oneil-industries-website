import React from 'react';
import Media from "./Media";
import {connect} from "react-redux";
import {fetchAlbum} from "../../../actions";
import Modal from "./Modal";
import EditForm from "../../formElements/editAlbumForm";

class UserAlbumsPage extends React.Component {

    state = { isModalOpen: false };

    handleShowDialog = (album) => {
        this.setState({ isOpen: !this.state.isOpen, album });
    };

    componentDidMount() {
        if (!this.props.medias.albums) {
            this.props.fetchAlbum(`/gallery/myalbums/${this.props.user}`);
        }
    }

    renderErrorMessage = () => {
        return (
            <div className="three column">
                <div className="ui error center aligned header">{this.props.medias.message}</div>
            </div>
        )
    };

    renderList = () => {
        if (this.props.medias.message) {
            return this.renderErrorMessage();
        }
        if (this.props.medias.albums) {
            return this.props.medias.albums.map(album => {
                return (
                    <div className="column imageBox" key={album.album.id}>
                        <h1 className="ui centered header">{album.album.name}</h1>
                        <Media media={album.media[0]} onClick={this.handleShowDialog.bind(this, album)}/>
                    </div>
                );
            })
        }
    };

    render() {
        return (
            <div>
                <h1 className="ui center aligned header">
                    {this.props.user}'s Albums
                </h1>
                <div className="ui three column grid">
                    {this.renderList()}
                </div>
                {this.state.isOpen && (<Modal
                    title={this.state.album.album.name}
                    closeModal = {() => this.handleShowDialog()}
                >
                    <div className="image">
                        <Media media={this.state.album.media[0]} renderVideoControls={true}/>
                    </div>
                    <div className="centerText">
                        <EditForm album={this.state.album.album}
                                  initialValues={{'name': this.state.album.album.name,
                            'showUnlistedImages' : this.state.album.album.showUnlistedImages}}/>
                    </div>
                </Modal>)}
            </div>
        );
    }
}
const mapStateToProps = (state) => {
    return {medias: state.medias};
};

export default connect(
    mapStateToProps,
    { fetchAlbum }
)(UserAlbumsPage);
