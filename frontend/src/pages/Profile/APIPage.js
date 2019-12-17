import React from 'react';
import {connect} from "react-redux";
import {generateAPIToken, generateShareXConfig, getAPIToken} from "../../actions/profile";
import DetailBox from "../../components/DetailBox";
import {CopyToClipboard} from "react-copy-to-clipboard";

class APIPage extends React.Component {

    state = {apiButtonText : "Copy", shareXButtonText : "Copy"};

    constructor(props) {
        super(props);

        if (!this.props.profile.apiToken) {
            this.props.getAPIToken("/user/profile/getAPIToken");
        }
        if (!this.props.profile.shareXConfig) {
            this.props.generateShareXConfig("/user/profile/getShareX");
        }
    }

    render() {
        const {apiToken, shareXConfig} = this.props.profile;

        return (
            <div className="ui padded grid">
                <div className="ui six wide column">
                    <DetailBox header="Api Token">
                        {apiToken && <div>
                            <div className="ui action fluid input">
                                <input type="text" readOnly value={apiToken}/>
                                <CopyToClipboard text={apiToken} onCopy={() => this.setState({apiButtonText: "Copied"})}>
                                    <button className="ui teal right labeled icon button">
                                        <i className="copy icon"/>
                                        {this.state.apiButtonText}
                                    </button>
                                </CopyToClipboard>
                                <button className="ui negative button">
                                    Delete
                                </button>
                            </div>
                        </div>}
                        <button className="ui centerButton primary button" onClick={() => {this.props.generateAPIToken("/user/profile/generateAPIToken")}}>
                            Generate
                        </button>
                    </DetailBox>
                </div>
                <div className="ui six wide column">
                    <DetailBox header="ShareX Config">
                        {shareXConfig && <div>
                            <div className="ui form">
                                <div className="field">
                                    <textarea readOnly value={JSON.stringify(shareXConfig)}/>
                                </div>
                            </div>
                        </div>}
                        <CopyToClipboard text={JSON.stringify(shareXConfig)} onCopy={() => this.setState({shareXButtonText: "Copied"})}>
                        <button className="ui teal right labeled icon centerButton button">
                            <i className="copy icon"/>
                            {this.state.shareXButtonText}
                        </button>
                        </CopyToClipboard>
                    </DetailBox>
                </div>
            </div>
        )
    }
}
const mapStateToProps = (state) => {
    return {profile: state.profile};
};

export default connect(
    mapStateToProps,
    { getAPIToken,
        generateShareXConfig,
        generateAPIToken}
)(APIPage);