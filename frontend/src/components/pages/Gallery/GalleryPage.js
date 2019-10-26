import React from 'react';
import {fetchImages} from "../../../actions";
import {connect} from "react-redux";

import '../../../assets/css/images.css';
import Media from "./Media";

class GalleryPage extends React.Component {

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
                    <div className="column imageBox" key={media.id}>
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