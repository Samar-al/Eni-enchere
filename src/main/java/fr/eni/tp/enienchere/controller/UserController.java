package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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

    @PostMapping(value="/profil")
    public String updateProfil(
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            Model model
    ) {
        try {
            userService.updateUser(user);
            return "redirect:/encheres/informations";
        } catch (BusinessException businessException) {
            businessException.getKeys().forEach(message -> {
                ObjectError error = new ObjectError("globalError", message);
                bindingResult.addError(error);
            });
            return "user/profil.html";
        }
    }

    @GetMapping(value="/informations/vendeur")
    public String displayVendeur(
        @RequestParam(value = "userId") String userId,
        Model model
    ) {
        long id = Long.parseLong(userId);
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user/details.html";
    }

    @GetMapping("/profil/password")
    public String showChangePasswordForm( @ModelAttribute("userSession") User userSession,Model model) {
        // Add any necessary model attributes
      /*  System.out.println(userService.getUserById(userSession.getUserNb()));
        model.addAttribute("user", userService.getUserById(userSession.getUserNb()));*/
        return "user/change-password"; // This should be the name of your change password page
    }

    @PostMapping(value = "/profil/password")
    public  String changePassword(@ModelAttribute User user,
                                  @RequestParam(name = "confirmPassword") String confirmPassword,
                                  Model model) {
       if (!user.getPassword().equals(confirmPassword)) {

            model.addAttribute("loginError", true);
            return "user/change-password";
        } else {

            userService.updatepassword(user);
            return "redirect:/encheres/profil";
        }
    }
}
