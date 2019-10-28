import React from 'react';
import {connect} from "react-redux";
import {deleteMedia, fetchUserImages} from "../../../actions";
import Media from "./Media";
import '../../../assets/css/layout.css';
import Modal from "./Modal";

class UserGalleryPage extends React.Component {

    state = { isModalOpen: false };

    handleShowDialog = (media) => {
        this.setState({ isOpen: !this.state.isOpen, media });
    };

    componentDidMount() {
        if (!this.props.medias.userMediasList) {
            this.props.fetchUserImages(`/gallery/medias/user/${this.props.user}`);
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
        if (this.props.medias.userMediasList) {
            return this.props.medias.userMediasList.map(media => {
                return (
                    <div className="column imageBox" key={media.id}>
                        <h1 className="ui center aligned header">{media.name}</h1>
                        <Media media={media} onClick={this.handleShowDialog.bind(this, media)}/>
                    </div>
                );
            })
        }
    };

    render() {
        return (
            <div>
                <h1 className="ui center aligned header">
                    {this.props.user}'s Gallery
                </h1>
                <div className="ui three column grid">
                    {this.renderList()}
                    {this.state.isOpen && (<Modal
                        title={this.state.media.name}
                        closeModal = {() => this.handleShowDialog()}
                    >
                        <div className="image">
                            <Media media={this.state.media} renderVideoControls={true}/>
                        </div>
                        <div className="centerText">
                            <p>Uploader: {this.state.media.uploader}</p>
                            <p>Uploaded: {this.state.media.dateAdded}</p>
                        </div>
                        <button value="Delete" className="centerButton ui negative button center aligned"
                                onClick={() => this.props.deleteMedia(`/gallery/media/delete/${this.state.media.id}`, this.state.media.id)}>
                            Delete
                        </button>
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
    { fetchUserImages, deleteMedia }
)(UserGalleryPage);