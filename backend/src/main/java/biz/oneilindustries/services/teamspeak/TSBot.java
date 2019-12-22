package biz.oneilindustries.services.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import org.springframework.stereotype.Component;

@Component
public class TSBot {

    private static TS3Api api;

    private TSBot() {
    }

    public static void start(String tsUsername, String tsPassword, String tsIPAddress) {
        final TS3Config config = new TS3Config();
        config.setHost(tsIPAddress);


        config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
        config.setConnectionHandler(new ConnectionHandler() {

            @Override
            public void onConnect(TS3Query ts3Query) {
                connectToServer(ts3Query.getApi(), tsUsername, tsPassword);
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                // Nothing
            }
        });
        final TS3Query query = new TS3Query(config);
        query.connect();
        api = query.getApi();
    }

    private static void connectToServer(TS3Api api, String tsUsername, String tsPassword) {
        api.login(tsUsername, tsPassword);
        api.selectVirtualServerById(1);
        api.setNickname("Oneil Status Bot");

        api.registerEvent(TS3EventType.TEXT_CHANNEL, 0);
    }

    public static TS3Api api() {
        return api;
    }
}