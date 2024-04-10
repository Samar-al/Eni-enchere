package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/encheres")
@SessionAttributes({"userSession"})
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String displayUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute(users);
        return "index.html";
    }

    @GetMapping(value="/informations")
    public String displayUser(
            @ModelAttribute("userSession") User userSession,
            Model model
    ) {
        model.addAttribute("user", userService.getUserById(userSession.getUserNb()));
        return "user/details.html";
    }

    @GetMapping(value="/profil")
    public String displayProfil(
            @ModelAttribute("userSession") User userSession,
            Model model
    ) {
        model.addAttribute("user", userService.getUserById(userSession.getUserNb()));
        return "user/profil.html";
    }
}
