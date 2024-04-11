package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bll.CategoryService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BidController {
    @Autowired
    BidService bidService;

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/")
    public String displayAllBids(Model model) {
        List<Bid> bids = bidService.getAllBids();
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("bids", bids);
        model.addAttribute("categories", categories);
        System.out.println(categories);
        return "index.html";
    }
}
