package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.service.AlbumService;
import biz.oneilindustries.website.service.MediaService;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.UpdatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final MediaService mediaService;
    private final AlbumService albumService;

    @Autowired
    public AdminController(UserService userService, MediaService mediaService, AlbumService albumService) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.albumService = albumService;
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        //Nothing is in set for admin yet. Still in planning
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users")
    public String showUsers(Model model) {

        List<User> users = userService.getUsers();

        model.addAttribute("users",users);

        return "/admin/users";
    }

    @GetMapping("/admin/user/{username}")
    public String manageUser(@PathVariable String username, Model model) {
        User user = userService.getUser(username);

        if (user == null) {
            throw new UsernameNotFoundException(username + " doesn't exists");
        }
        model.addAttribute("user", new UpdatedUser(user.getUsername(),user.getEmail(),user.getAuthorities().get(0).getAuthority(),user.getEnabled()));
        model.addAttribute("mediaCount", mediaService.getTotalMediaCountByUser(username));
        model.addAttribute("albumCount",albumService.getAlbumCountByUser(username));
        model.addAttribute("teamspeakProfiles", userService.getUserTeamspeakProfile(username));
        model.addAttribute("discordProfiles", userService.getUserDiscordProfiles(username));

        return "/admin/manageuser";
    }

    @PostMapping("/admin/updateUser/{username}")
    public String updateUser(@ModelAttribute @Valid UpdatedUser updatedUser, @PathVariable String username) {

        userService.updateUser(updatedUser,username);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user/{username}/hideMedia")
    public String hideUserMedia(@PathVariable String username) {
        mediaService.hideAllMedia(username);

        return "redirect:/admin/user/" + username;
    }

}
