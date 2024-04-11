package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bll.CategoryService;

import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.User;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.*;
import fr.eni.tp.enienchere.exception.BusinessException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.sql.Types.NULL;

@Controller
@RequestMapping(value = "/encheres")
@SessionAttributes({"categorySession"})
public class BidController {

    private BidService bidService;
    private CategoryService categoryService;

    private UserService userService;

    public BidController(BidService bidService, CategoryService categoryService, UserService userService) {
        this.bidService = bidService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String displayAllBids(Model model) {
        List<Bid> bids = bidService.getAllBids();
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("bids", bids);
        model.addAttribute("categories", categories);
        return "index.html";
    }

    @GetMapping(value = ("/creer-vente"))
    public String CreateBid(Principal principal, @ModelAttribute("categorySession") Category categorySession, Model model) throws ParseException {
        Bid bid = new Bid();
        SoldItem soldItem = new SoldItem();
        soldItem.setItemName(" ");
        soldItem.setDescription(" ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateStartBid = dateFormat.parse("2022-01-02 10:00:00");
        soldItem.setDateStartBid(dateStartBid);
        soldItem.setDateEndBid(dateStartBid);
        soldItem.setInitialPrice(30);
        soldItem.setSalePrice(30);
        if(principal != null) {
            String currentUser = principal.getName();
            soldItem.setSoldUser(userService.getUser(currentUser));
        }
        CollectParcel collectParcel = new CollectParcel();
        collectParcel.setStreet(" ");
        collectParcel.setZipCode(" ");
        collectParcel.setCity(" ");
        soldItem.setCollectParcel(collectParcel);
        bid.setSoldItem(soldItem);
        model.addAttribute("bid", bid);
        model.addAttribute("soldItem",soldItem );
        return "bid/create";
    }

    @PostMapping(value ="/creer-vente")
    public String createOrUpdateBid(@Valid @ModelAttribute("bid") Bid bid, BindingResult bindingResult, Principal principal) {

        if(principal == null) {
           return "redirect:/encheres/";
        }else {
            String currentUserName = principal.getName();

            if (bindingResult.hasErrors()) {
                return "bid/create";
            } else {
                try {
                   /* if (bid.getUser() != null && bid.getSoldItem() != null) {
                        // Update existing movie
                        *//*   bidService.updateBid(bid, loggedUser);*//*
                    } else {*/
                        // Create new movie
                    bidService.createBid(bid, currentUserName);


                   // }
                    return "redirect:/encheres/";
                } catch (BusinessException businessException) {
                    businessException.getKeys().forEach(message -> {
                        ObjectError error = new ObjectError("globalError", message);
                        bindingResult.addError(error);
                    });
                    return "bid/create";

                }
            }
        }
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return ((User) principal).getUserNb();
            }
        }
        return null; // No connected user or user is not authenticated
    }

    @ModelAttribute("categorySession")
    public List<Category> leadSessionCategory() {
        List<Category>categories = categoryService.getCategories();
        return categories;
    }
}
