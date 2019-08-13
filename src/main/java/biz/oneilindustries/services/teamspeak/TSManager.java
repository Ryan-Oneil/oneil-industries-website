package biz.oneilindustries.services.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TSManager {

    private TS3Api ts3Api;

    public TSManager() {
        this.ts3Api = TSBot.api();
    }

    public List<Client> getOnlineUsers() {
        this.ts3Api = TSBot.api();

        return ts3Api.getClients();
    }

    public List<Channel> getServerChannels() {
        this.ts3Api = TSBot.api();

        return ts3Api.getChannels();
    }
}
