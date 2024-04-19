package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {


    @Autowired
    private UserService userService;
    @GetMapping("/inscription")
    public String index(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register.html";
    }

    @PostMapping(value = "/inscription")
    public String register(@Valid @ModelAttribute User user,
                           BindingResult bindingResult,
                           @RequestParam(name = "confirmPassword") String confirmPassword,
                           Model model) {

        if(bindingResult.hasErrors()){
            return "register.html";
        } else if(!user.getPassword().equals(confirmPassword)){
            model.addAttribute("loginError", true);
            return "register.html";
        } else {
            try {
                userService.addUser(user);
                return "redirect:/login";
            } catch (DuplicateKeyException e) {
                String errorMessage;
                if (e.getMessage().contains("username_UNIQUE")) {
                    errorMessage = "error.usernameExists";
                } else if (e.getMessage().contains("email_UNIQUE")) {
                    errorMessage = "error.emailExists";
                } else {
                    errorMessage = "error.registration";
                }
                model.addAttribute("errorMessage", errorMessage);
                return "register";

            }
        }
    }
}
