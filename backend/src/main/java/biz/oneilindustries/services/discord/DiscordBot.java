package biz.oneilindustries.services.discord;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

public class DiscordBot {

    private static JDA jda;
    private static final String GUILD_ID = "367725161052372993";

    private DiscordBot() {
    }

    public static void start(String botToken) {
        try {
            jda = JDABuilder.createDefault(botToken).build();
            //jda.addEventListener(new DiscordCommandListener());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static JDA getJda() {
        return jda;
    }

    public static Guild getGuild() {
        return jda.getGuildById(GUILD_ID);
    }
}
