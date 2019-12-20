import React from "react";
import { connect } from "react-redux";
import {
  getDiscordActiveList,
  getTeamspeakActiveList
} from "../actions/services";
import ServiceList from "../components/Services/ServiceList";
import { renderErrorMessage } from "../components/Message";

class Services extends React.Component {
  constructor(props) {
    super(props);

    this.props.getTeamspeakActiveList("/services/public/teamspeak");
    this.props.getDiscordActiveList("/services/public/discord");
  }

  render() {
    return (
      <div className="ui two column stackable center aligned page grid">
        <div className="column eight wide">
          <div className="teamspeakList ui segment ">
            <h1 className="ui center aligned header">
              <a href="ts3server://oneilindustries.biz/?port=9987">
                Connect to Teamspeak
              </a>
            </h1>
            {this.props.services.activeTSList && (
              <ServiceList services={this.props.services.activeTSList} />
            )}
          </div>
        </div>
        <div className="column eight wide">
          <div className="teamspeakList ui segment ">
            <h1 className="ui center aligned header">
              <a href="https://discord.gg/TSYe5vX">Connect to Discord</a>
            </h1>
            {this.props.services.activeTSList && (
              <ServiceList services={this.props.services.activeDiscord} />
            )}
          </div>
        </div>
        {this.props.services.errorMessage &&
          renderErrorMessage(this.props.services.errorMessage)}
      </div>
    );
  }
}

const mapStateToProps = state => {
  return { services: state.services };
};

export default connect(mapStateToProps, {
  getTeamspeakActiveList,
  getDiscordActiveList
})(Services);
