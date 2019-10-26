import React from 'react';
import MediaModal from "./MediaModal";

class Media extends React.Component {
    state = { isOpen: false };

    handleShowDialog = () => {
        this.setState({ isOpen: !this.state.isOpen });
    };

    render() {
        const {media} = this.props;

        const src = `http://localhost:8080/api/gallery/media/thumbnail/${media.id}`;

        return <div className="column imageBox" key={media.id}>
            <div className="ui">
                <h2 className="ui center aligned header">{media.name}</h2>
                <img src={src} onClick={this.handleShowDialog}/>
            </div>
            {this.state.isOpen && (<MediaModal
                media={media}
                src={src}
                closeModal = {() => this.handleShowDialog()}
            />)}
        </div>
    }
}

export default Media;