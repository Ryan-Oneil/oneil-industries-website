package biz.oneilenterprise.website.config;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoiceServiceConfig {

    @Value("${service.voice.discord.apiKey}")
    private String discordApiKey;

    @Bean
    public JDA getJDA() throws LoginException, InterruptedException {
        return JDABuilder.createDefault(discordApiKey).build().awaitReady();
    }
}
