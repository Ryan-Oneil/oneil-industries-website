import React from 'react';
import {connect} from "react-redux";
import {fetchAlbum} from "../../../actions";
import Media from "./Media";

class Album extends React.Component {

    componentDidMount() {
        const { match: { params } } = this.props;
        this.props.fetchAlbum(`/gallery/album/${params.albumName}`);
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
            return this.props.medias.albums.map(media => {
                return (
                    <div className="column imageBox" key={media.id}>
                        <Media media={media}/>
                    </div>
                );
            })
        }
    };

    render() {
        const { match: { params } } = this.props;
        return (
            <div>
                <h1 className="ui center aligned header">
                    Album : {params.albumName}
                </h1>
                <div className="ui three column grid">
                    {this.renderList()}
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
    { fetchAlbum }
)(Album);