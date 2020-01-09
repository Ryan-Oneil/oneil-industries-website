import React from "react";
import channelIcon from "../../assets/images/teamspeakChat.png";
import userIcon from "../../assets/images/user.png";

export default serviceList => {
  return serviceList.list.map(channel => {
    const cssClass = channel.parentChannelID === 0 ? "channel" : "subChannel";

    if (channel.name === "") {
      return;
    }

    return (
      <div key={channel.name}>
        <div className={cssClass}>
          <p>
            <img src={channelIcon} alt="Channel icon" />
            {channel.name}
          </p>
        </div>
        {renderClients(channel.usersInChannel)}
      </div>
    );
  });
};

const renderClients = clients => {
  return clients.map(client => {
    return (
      <div className="user" key={client.uuid}>
        <p>
          <img src={userIcon} alt="user icon" />
          {client.name}
        </p>
      </div>
    );
  });
};
