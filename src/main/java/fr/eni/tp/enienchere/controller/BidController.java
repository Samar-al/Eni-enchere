package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static java.sql.Types.NULL;

@Controller
@RequestMapping(value = "/encheres")
public class BidController {

    @Autowired
    BidService bidService;

    @PostMapping(value = "/detail-item/{item_id}")
    public String submitBid(@Valid @ModelAttribute("bid") Bid newBid,
                            BindingResult bindingResult,
                            Principal principal,
                            @PathVariable(name = "item_id") String item_id,
                            Model model
    ) {
            String currentUserName = principal.getName();
            bidService.placeBid(newBid, currentUserName, item_id);
            return "redirect:/bids"; // Redirect to a page showing all bids

    }


}
