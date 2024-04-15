package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import static java.sql.Types.NULL;

@Controller
@RequestMapping(value = "/encheres")
public class BidController {

    @Autowired
    BidService bidService;

    @Autowired
    SoldItemService soldItemService;

    @Autowired
    UserService userService;

    @PostMapping(value = "/detail-item/{item_id}")
    public String submitBid(@Valid @ModelAttribute("bid") Bid newBid,
                            BindingResult bindingResult,
                            Principal principal,
                            @PathVariable(name = "item_id") String item_id,
                            Model model,
                            RedirectAttributes redirectAttributes
    ) {
        String currentUserName = principal.getName();
        int idItem = Integer.parseInt(item_id);
        SoldItem soldItem = soldItemService.getSoldItemById(idItem);
        Bid bid = bidService.getBidByItemId(idItem);
        try {

            bidService.placeBid(newBid, currentUserName, item_id);
            redirectAttributes.addFlashAttribute("successMessage", "Your bid was placed successfully.");
            return "redirect:/encheres/detail-item/{item_id}"; // Redirect to a page showing all bids

        } catch (BusinessException businessException) {
            businessException.getKeys().forEach(message -> {
                ObjectError error = new ObjectError("globalError", message);
                bindingResult.addError(error);
            });
            model.addAttribute("soldItem", soldItem);
            //model.addAttribute("bid", bid);
            return "soldItem/details";
        }
    }

}
