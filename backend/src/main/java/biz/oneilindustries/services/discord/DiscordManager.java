package biz.oneilindustries.services.discord;

import biz.oneilindustries.rank.Rank;
import biz.oneilindustries.website.pojo.CustomChannel;
import biz.oneilindustries.website.pojo.ServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
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

    public List<CustomChannel> getChannelsMapped() {
        List<Category> categories = getCategories();

        return categories.stream()
            .filter(category -> !category.getVoiceChannels().isEmpty())
            .map(category -> {
                CustomChannel parentCategory = new CustomChannel(category.getName(), category.getId());
                List<VoiceChannel> voiceChannels = category.getVoiceChannels();

                voiceChannels.forEach(voiceChannel ->  {
                    CustomChannel discordChannel = new CustomChannel(voiceChannel.getName(), voiceChannel.getId());
                    List<ServiceClient> usersInChannel = voiceChannel.getMembers().stream()
                        .map(member -> new ServiceClient(member.getEffectiveName(), member.getId()))
                        .collect(Collectors.toList());

                    discordChannel.setUsersInChannel(usersInChannel);
                    parentCategory.addChild(discordChannel);
                });
                return parentCategory;
            }).collect(Collectors.toList());
    }

    public void sendUserMessage(String uuid, String message) {

        guild = DiscordBot.getGuild();

        guild.getJDA().getUserById(uuid).openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
    }

    public String getUserName(String uuid) {

        guild = DiscordBot.getGuild();

        return guild.getJDA().getUserById(uuid).getName();
    }

    public Member getMember(String uuid) {

        guild = DiscordBot.getGuild();

        return guild.getMemberById(uuid);
    }

    public void addUserRole(Member member, String roleName) {

        guild = DiscordBot.getGuild();

        Rank.setDiscordServerRoles(guild.getRoles());
        Role role = Rank.getRequiredDiscordRole(roleName);

        if (role == null) {
            return;
        }
        guild.addRoleToMember(member, role).queue();
    }

    public void removeRoles(Member member) {

        guild = DiscordBot.getGuild();

        guild.modifyMemberRoles(member, new ArrayList<>()).queue();
    }
}
