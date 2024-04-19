package fr.eni.tp.enienchere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToEncheres() {
        return "redirect:/encheres/";
    }
}
