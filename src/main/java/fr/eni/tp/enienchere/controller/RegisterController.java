package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        return "register";
    }

    @PostMapping(value = "/inscription")
    public String register(@Valid @ModelAttribute User user,
                           BindingResult bindingResult,
                           @RequestParam(name = "confirmPassword") String confirmPassword,
                           Model model) {

        if(bindingResult.hasErrors()){
            return "register";
        } else if(!user.getPassword().equals(confirmPassword)){
            model.addAttribute("loginError", true);
            return "register";
        } else {
            try {
                userService.addUser(user);
                return "redirect:/login";
            } catch (BusinessException businessException) {
                businessException.getKeys().forEach(message -> {
                    ObjectError error = new ObjectError("globalError", message);
                    bindingResult.addError(error);
                });
                return "register";
            }

        }

    }
}
