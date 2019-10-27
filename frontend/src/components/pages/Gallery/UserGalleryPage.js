import React from 'react';
import {connect} from "react-redux";
import {deleteMedia, fetchImages} from "../../../actions";
import Media from "./Media";
import '../../../assets/css/layout.css';

class UserGalleryPage extends React.Component {

    componentDidMount() {
        this.props.fetchImages(`/gallery/medias/user/${this.props.user}`);
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
                    <div className="column imageBox" key={media.id}>
                        <Media media={media}>
                            <button value="Delete" className="centerButton ui negative button center aligned" onClick={() => this.props.deleteMedia(`/gallery/media/delete/${media.id}`, media.id) }>Delete</button>
                            {this.props.medias.deleteError && <div className="ui error message">
                                {<div className="header">{this.props.medias.deleteError}</div>}
                            </div>}
                        </Media>
                    </div>
                );
            })
        }
    };

    render() {
        return (
            <div className="ui three column grid">
                {this.renderList()}
            </div>
        );
    }
}
const mapStateToProps = (state) => {
    return {medias: state.medias};
};

export default connect(
    mapStateToProps,
    { fetchImages, deleteMedia }
)(UserGalleryPage);