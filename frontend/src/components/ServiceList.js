import React from "react";
import channelIcon from '../assets/images/teamspeakChat.png';
import userIcon from '../assets/images/user.png';

export default (serviceList) => {
    if (serviceList.services) {
        return serviceList.services.map(channel => {

            const cssClass = channel.parentChannelID === 0 ? "channel" : "subChannel";

            if (channel.name === "") {
                return
            }

            return (
                <div key={channel.name}>
                    <div className={cssClass}>
                        <p><img src={channelIcon} alt="Channel icon"/>{channel.name}</p>
                    </div>
                    {channel.usersInChannel && renderClients(channel.usersInChannel)}
                </div>
            )
        })
    }
};

const renderClients = (clients) => {
    if (clients) {
        return clients.map(client => {
            return (
                <div className="user" key={client.uuid}>
                    <p><img src={userIcon} alt="user icon"/>{client.name}</p>
                </div>
            )
        })
    }
};