package biz.oneilenterprise.website.voiceservices.discord;

import biz.oneilenterprise.website.dto.CustomChannelDTO;
import biz.oneilenterprise.website.dto.ServiceClientDTO;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscordManager {

    private final Guild guild;

    public DiscordManager(JDA jda, @Value("${service.voice.discord.guildId}") String guildId) {
        this.guild = jda.getGuildById(guildId);
    }

    public List<Category> getCategories() {
        return guild.getCategories();
    }

    public List<CustomChannelDTO> getChannelsMapped() {
        List<Category> categories = getCategories();

        return categories.stream()
            .filter(category -> !category.getVoiceChannels().isEmpty())
            .map(category -> {
                CustomChannelDTO parentCategory = new CustomChannelDTO(category.getName(), category.getId());
                List<VoiceChannel> voiceChannels = category.getVoiceChannels();

                voiceChannels.forEach(voiceChannel ->  {
                    CustomChannelDTO discordChannel = new CustomChannelDTO(voiceChannel.getName(), voiceChannel.getId());
                    List<ServiceClientDTO> usersInChannel = voiceChannel.getMembers().stream()
                        .map(member -> new ServiceClientDTO(member.getEffectiveName(), member.getId()))
                        .collect(Collectors.toList());

                    discordChannel.setUsersInChannel(usersInChannel);
                    parentCategory.addChild(discordChannel);
                });
                return parentCategory;
            }).collect(Collectors.toList());
    }
}
