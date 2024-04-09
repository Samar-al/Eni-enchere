package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {


    @Autowired
    private UserService userService;
    @GetMapping("/inscription")
    public String index(Model model) {
        return "register";
    }

    @PostMapping(value = "/inscription")
    public String register(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        try {
            if(!bindingResult.hasErrors()) {
                userService.addUser(user);
                return "redirect:/login";
            }
        } catch (Exception e) {
            // ajout de l'erreur dans validation de plainPassword
          /*  bindingResult.addError(new FieldError("bllValidation",
                    "plainPassword",
                    e.getMessage()));*/

        }
        // en cas d'erreur
       /* model.addAttribute("roles", Role.values());*/
        return "register";

    }
}
