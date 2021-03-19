package biz.oneilenterprise.website.config;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoiceServiceConfig {

    @Value("${service.voice.teamspeak.host}")
    private String tsHost;

    @Value("${service.voice.teamspeak.username}")
    private String tsUsername;

    @Value("${service.voice.teamspeak.password}")
    private String tsPassword;

    @Value("${service.voice.discord.apiKey}")
    private String discordApiKey;

    private static final Logger logger = LogManager.getLogger(VoiceServiceConfig.class);

    @Bean
    public JDA getJDA() throws LoginException, InterruptedException {
        return JDABuilder.createDefault(discordApiKey).build().awaitReady();
    }

    public TS3Config getTS3Config() {
        final TS3Config config = new TS3Config();
        config.setHost(tsHost);

        config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
        config.setConnectionHandler(new ConnectionHandler() {

            @Override
            public void onConnect(TS3Query ts3Query) {
                TS3Api api = ts3Query.getApi();
                api.login(tsUsername, tsPassword);
                api.selectVirtualServerById(1);
                api.setNickname("Oneil Status Bot");

                api.registerEvent(TS3EventType.TEXT_CHANNEL, 0);
                logger.info("TS API has connected");
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                logger.warn("TS API has been disconnected");
            }
        });
        return config;
    }

    @Bean
    public TS3Api getTS3Api() {
        final TS3Query query = new TS3Query(getTS3Config());
        query.connect();

        return query.getApi();
    }
}
