package biz.oneilindsutries.website.controller;

import biz.oneilindsutries.website.entity.User;
import biz.oneilindsutries.website.service.UserService;
import biz.oneilindsutries.website.validation.UpdatedUser;
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

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admmin")
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

        return "/admin/manageuser";
    }

    @PostMapping("/admin/updateUser/{username}")
    public String updateUser(@ModelAttribute @Valid UpdatedUser updatedUser, @PathVariable String username) {

        userService.updateUser(updatedUser,username);

        return "redirect:/admin/users";
    }

}
