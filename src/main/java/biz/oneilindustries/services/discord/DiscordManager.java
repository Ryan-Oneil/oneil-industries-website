package biz.oneilindustries.services.discord;

import java.util.List;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Component;

@Component
public class DiscordManager {

    private Guild guild;

    public List<Category> getCategories() {

        guild = DiscordBot.getGuild();

        return guild.getCategories();
    }

    public List<Member> getMembers() {

        guild = DiscordBot.getGuild();

        return guild.getMembers();
    }

    public void sendUserMessage(String uuid, String message) {

        guild = DiscordBot.getGuild();

        guild.getJDA().getUserById(uuid).openPrivateChannel().queue((channel) -> channel.sendMessage(message).queue());
    }

    public String getUserName(String uuid) {

        guild = DiscordBot.getGuild();

        return guild.getJDA().getUserById(uuid).getName();
    }
}
