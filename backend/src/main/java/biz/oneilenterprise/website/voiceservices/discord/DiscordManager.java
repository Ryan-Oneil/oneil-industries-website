package biz.oneilenterprise.website.voiceservices.discord;

import biz.oneilenterprise.website.rank.Rank;
import biz.oneilenterprise.website.pojo.CustomChannel;
import biz.oneilenterprise.website.pojo.ServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
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

    public List<Member> getMembers() {
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
        guild.getJDA().getUserById(uuid).openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
    }

    public String getUserName(String uuid) {
        return guild.getJDA().getUserById(uuid).getName();
    }

    public Member getMember(String uuid) {
        return guild.getMemberById(uuid);
    }

    public void addUserRole(Member member, String roleName) {
        Rank.setDiscordServerRoles(guild.getRoles());
        Role role = Rank.getRequiredDiscordRole(roleName);

        if (role == null) {
            return;
        }
        guild.addRoleToMember(member, role).queue();
    }

    public void removeRoles(Member member) {
        guild.modifyMemberRoles(member, new ArrayList<>()).queue();
    }
}
