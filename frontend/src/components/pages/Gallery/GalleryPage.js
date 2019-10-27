import React from 'react';
import {fetchImages} from "../../../actions";
import {connect} from "react-redux";

import '../../../assets/css/images.css';
import Media from "./Media";
import Modal from "./Modal";

class GalleryPage extends React.Component {

    state = { isModalOpen: false };

    handleShowDialog = (media) => {
        this.setState({ isOpen: !this.state.isOpen, media });
    };

    componentDidMount() {
        this.props.fetchImages("/gallery/medias");
    }

    renderErrorMessage = () => {
        return(
            <div className="three column">
                <div className="ui error center aligned header">{this.props.medias.message}</div>
            </div>
        )
    };

    renderList = () => {
        if (this.props.medias.message) {
                return this.renderErrorMessage();
            }
        if (this.props.medias.mediasList) {
            return this.props.medias.mediasList.map(media => {
                return (
                    <div className="column imageBox" key={media.id} onClick={this.handleShowDialog.bind(this, media)}>
                        <h1 className="ui center aligned header">{media.name}</h1>
                        <Media media={media}/>
                    </div>
                );
            })
        }
    };

    render() {
        return (
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
                        {this.props.children}
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
    { fetchImages }
)(GalleryPage);