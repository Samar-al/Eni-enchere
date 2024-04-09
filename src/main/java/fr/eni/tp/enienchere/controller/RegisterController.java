package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bo.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class RegisterController {
    @GetMapping("/inscription")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
}
