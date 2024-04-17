package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;

@Controller
@SessionAttributes({"userSession"})
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/login-error")
    public String login(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @GetMapping("/login/details")
    public String login(
            @ModelAttribute("userSession") User userSession,
            @RequestParam(required = false) boolean saveMe,
            HttpServletResponse response,
            Principal principal
    ) {
        User user = userService.getUserByUsername(principal.getName());

        if (user != null) {
            userSession.setUserNb(user.getUserNb());
            userSession.setUsername(user.getUsername());
            userSession.setLastname(user.getLastname());
            userSession.setFirstname(user.getFirstname());
            userSession.setEmail(user.getEmail());
            userSession.setPhone(user.getPhone());
            userSession.setStreet(user.getStreet());
            userSession.setZipCode(user.getZipCode());
            userSession.setCity(user.getCity());
            userSession.setAdmin(user.isAdmin());
            System.out.println("passé dans l'éxecution");
            System.out.println(saveMe);
            if (saveMe) {
                String jsScript = "localStorage.setItem('username', '" + userSession.getUsername() + "');";
                jsScript += "localStorage.setItem('password', '" + userSession.getPassword() + "');";
                response.setHeader("Set-Cookie", "saveMe=true; Max-Age=2592000; Path=/"); // Expire après 30 jours

                System.out.println(jsScript);


                return "redirect:/encheres?script=" + jsScript;
            }

        } else {
            userSession.setUserNb(0);
            userSession.setUsername(null);
            userSession.setLastname(null);
            userSession.setFirstname(null);
            userSession.setEmail(null);
            userSession.setPhone(null);
            userSession.setStreet(null);
            userSession.setZipCode(null);
            userSession.setCity(null);
            userSession.setAdmin(false);
        }

        return "redirect:/encheres/";
    }

    @ModelAttribute("userSession")
    public User getUser() {
        return new User();
    }
}