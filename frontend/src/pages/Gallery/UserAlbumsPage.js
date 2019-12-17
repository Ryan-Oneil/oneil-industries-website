import React from 'react';
import Media from "../../components/Gallery/Media";
import {connect} from "react-redux";
import {fetchAlbums} from "../../actions";
import Modal from "../../components/Gallery/Modal";
import EditForm from "../../components/formElements/editAlbumForm";
import {renderErrorMessage} from "../../components/Message";

class UserAlbumsPage extends React.Component {

    state = { isModalOpen: false };

    handleShowDialog = (album) => {
        this.setState({ isOpen: !this.state.isOpen, album });
    };

    componentDidMount() {
        this.props.fetchAlbum(`/gallery/myalbums/${this.props.user}`);
    }

    renderList = () => {
        if (this.props.medias.message) {
            return renderErrorMessage(this.props.medias.message);
        }
        if (this.props.medias.albums) {
            return this.props.medias.albums.map(album => {
                return (
                    <div className="column imageBox" key={album.album.id}>
                        <div className="ui card">
                            <Media media={album.media[0]} onClick={this.handleShowDialog.bind(this, album)}/>
                            <h1 className="ui centered header">{album.album.name}</h1>
                        </div>
                    </div>
                );
            })
        }
    };

    render() {
        return (
            <div className="marginPadding">
                <h1 className="ui center aligned header">
                    {this.props.user}'s Albums
                </h1>
                <div className="ui container">
                    <div className="ui four column grid">
                        {this.renderList()}
                    </div>
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
                                  initialValues={{'newAlbumName': this.state.album.album.name,
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
    { fetchAlbum: fetchAlbums }
)(UserAlbumsPage);
