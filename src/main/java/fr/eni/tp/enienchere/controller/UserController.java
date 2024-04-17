package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.Token;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/encheres")
@SessionAttributes({"userSession"})
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

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

    @GetMapping(value = "/reset-password")
    public String getResetPasswordForm(@ModelAttribute(value = "token") String token, Model model) {
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        System.out.println(today);
        Token tokenUser = userService.getTokenUser(token);
        System.out.println(tokenUser.getExpiryDate());
        if (tokenUser.getToken().isEmpty()) {
            model.addAttribute("resetError", true);
            return "user/reset-password.html";
        }
//        if (tokenUser.getExpiryDate().isAfter(today)) {
//            model.addAttribute("resetError", true);
//            return "user/reset-password.html";
//        }

        model.addAttribute("tokenSession", tokenUser);

        return "user/reset-password.html";
    }

    @PostMapping(value = "/reset-password")
    public String resetPassword(@RequestParam(name = "tokenSession") String token,
            @RequestParam(name = "confirmPassword") String confirmPassword,
            Model model){

        try {
            Token tokenUser = userService.getTokenUser(token);

            User user = userService.getUserById(tokenUser.getUser().getUserNb());

            if (user == null) {
                model.addAttribute("resetError", true);
                return "user/reset-password.html";
            }

            user.setPassword(confirmPassword);

            userService.updatepassword(user);

            return "redirect:/encheres/";
        } catch (BusinessException businessException) {
            List<String> errorMessages = businessException.getKeys().stream()
                    .map(key -> messageSource.getMessage(key, null, LocaleContextHolder.getLocale()))
                    .collect(Collectors.toList());;
            model.addAttribute("resetError", errorMessages);

            return "user/reset-password.html";
        }

    }

    @GetMapping(value = "/forgot-password")
    public String getForgotPasswordForm(){
        return "user/forgot-password.html";
    }

    @PostMapping(value = "/forgot-password")
    public String forgotPassword(@RequestParam("email") String email,
                                 Model model){
        try {
            User user = userService.getUserByEmail(email);

            if(user == null ){
                model.addAttribute("forgotError", true);
                return "user/forgot-password";
            }

            // Générer un UUID aléatoire
            UUID uuid = UUID.randomUUID();
            // Convertir l'UUID en chaîne de caractères
            String tokenChar = uuid.toString();

            LocalDateTime expiryDate = LocalDateTime.now().plusHours(1) ;

            Token token = new Token();
            token.setUser(user);
            token.setToken(tokenChar);
            token.setExpiryDate(expiryDate);

            userService.createTokenUser(token);

            return "redirect:/encheres/reset-password?token="+ token.getToken();
        } catch (BusinessException businessException) {
            List<String> errorMessages = businessException.getKeys().stream()
                    .map(key -> messageSource.getMessage(key, null, LocaleContextHolder.getLocale()))
                    .collect(Collectors.toList());;
            model.addAttribute("forgotError", errorMessages);

            return "user/forgot-password.html";
        }



    }
}
