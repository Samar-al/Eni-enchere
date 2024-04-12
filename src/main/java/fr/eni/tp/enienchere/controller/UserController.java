package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/encheres")
@SessionAttributes({"userSession"})
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

  /*  @GetMapping(value = "/")
    public String displayUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute(users);
        return "index.html";
    }*/





    @GetMapping(value="/informations")
    public String displayUser(
            Principal principal,
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

        //Vérifie si le compte user supprimé, on ne donne pas assez à la page d'informations d'un vendeur
        if (user.getUsername().equals("Utilisateur supprimé")
                && user.getLastname().equals("none")
                && user.getFirstname().equals("none")
                && user.getEmail().equals("none@none.com")
        ) {
            return "redirect:/encheres/";
        } else {
            model.addAttribute("user", user);
            return "user/details.html";
        }
    }

    @GetMapping(value="/supprimerCompte")
    public String deleteUser(
        @ModelAttribute("userSession") User userSession,
        Model model
    ) {
        userService.deleteUser(userSession);
        return "redirect:/logout";
    }
}
