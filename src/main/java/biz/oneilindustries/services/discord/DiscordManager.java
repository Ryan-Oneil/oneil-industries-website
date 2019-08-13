package biz.oneilindustries.services.discord;

import java.util.List;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Component;

@Component
public class DiscordManager {

    private Guild guild;

    public List<Category> getCategories() {

        guild = DiscordBot.getGuild();

        return guild.getCategories();
    }
}
