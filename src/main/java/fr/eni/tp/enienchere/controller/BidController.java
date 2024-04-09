package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bo.Bid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BidController {

    @GetMapping(value = "/")
    public String displayAllBids(Model model) {

    }
}
