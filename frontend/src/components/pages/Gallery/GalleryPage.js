import React from 'react';
import {fetchImages} from "../../../actions";
import {connect} from "react-redux";

import '../../../assets/css/images.css';
import Media from "./Elements/Media";
import Modal from "./Elements/Modal";
import RenderMedias from "./Elements/RenderMedias";

class GalleryPage extends React.Component {

    state = { isModalOpen: false };

    handleShowDialog = (media) => {
        this.setState({ isOpen: !this.state.isOpen, media });
    };

    componentDidMount() {
        if (!this.props.medias.mediasList) {
            this.props.fetchImages("/gallery/medias");
        }
    }

    render() {
        return (
            <div className="marginPadding">
                <div className="ui container">
                    <div className="ui four column grid">
                        {RenderMedias(this.props.medias.mediasList,this.props.medias.message, this.handleShowDialog)}
                    </div>
                </div>
                {this.state.isOpen && (<Modal
                    title={this.state.media.name}
                    closeModal = {() => this.handleShowDialog()}
                >
                    <div className="image">
                        <a href={`http://localhost:8080/api/gallery/${this.state.media.mediaType}/${this.state.media.fileName}`}>
                            <Media media={this.state.media} renderVideoControls={true} fullSize={true}/>
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

const mapStateToProps = (state) => {
    return {medias: state.medias};
};

export default connect(
    mapStateToProps,
    { fetchImages }
)(GalleryPage);