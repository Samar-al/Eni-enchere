package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bll.CategoryService;

import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.User;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.*;
import fr.eni.tp.enienchere.exception.BusinessException;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/encheres")
@SessionAttributes({"categorySession", "userSession"})
public class SoldItemController {

    private SoldItemService soldItemService;
    private CategoryService categoryService;

    private BidService bidService;

    private UserService userService;

    public SoldItemController(SoldItemService soldItemService, CategoryService categoryService, UserService userService, BidService bidService) {
        this.soldItemService = soldItemService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.bidService = bidService;
    }



    @GetMapping(value = "/")
    public String displayAllSoldItem(
            @ModelAttribute("categorySession") Category categorySession,
            Model model
    ) {
        List<SoldItem> soldItems = soldItemService.getAllSoldItems();
        model.addAttribute("soldItems", soldItems);
        return "home/index.html";
    }

    @GetMapping(value = ("/creer-vente"))
    public String CreateSoldItem(Principal principal, @ModelAttribute("categorySession") Category categorySession, Model model) throws ParseException {

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

        model.addAttribute("soldItem",soldItem );
        return "soldItem/create.html";
    }

    @PostMapping(value = "/creer-vente")
    public String createOrUpdateSoldItem(@Valid @ModelAttribute("soldItem") SoldItem soldItem,
                                    BindingResult bindingResult,
                                    Principal principal,
                                    @RequestParam("picture") MultipartFile file,
                                         RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/encheres/";
        } else {
            String currentUserName = principal.getName();

            if (bindingResult.hasErrors()) {
                return "soldItem/create.html";
            } else {
                try {
                    Long itemNb = soldItem != null ? soldItem.getItemNb() : null;
                    if (itemNb == 0) {
                        if (file.isEmpty()) {
                            // Handle empty file
                            redirectAttributes.addFlashAttribute("errorMessage", "Please select a picture");
                            return "redirect:/encheres/creer-vente";
                        }
                        // Continue with your business logic
                        Long newItemId = soldItemService.create(soldItem, currentUserName);
                        savePicture(file, currentUserName, newItemId);
                        return "redirect:/encheres/";
                    }else{
                        System.out.println(soldItem);
                        if (!file.isEmpty()) {

                            savePicture(file, currentUserName, itemNb);
                        }
                        soldItemService.update(soldItem);
                       return "redirect:/encheres/detail-item/" + itemNb;
                    }
                } catch (IOException e) {
                    // Handle file writing exception
                    e.printStackTrace();
                    return "redirect:/error";
                }
            }
        }
    }

    @GetMapping(value = ("/detail-item/{item_id}"))
    public String displaySoldItem(@PathVariable(name = "item_id") String item_id,
                                  @ModelAttribute("categorySession") Category categorySession,
                                  @ModelAttribute("userSession") User userSession,
                                  Model model
    ) {
            int idItem = Integer.parseInt(item_id);
            SoldItem soldItem = soldItemService.getSoldItemById(idItem);
            Bid bid = bidService.getBidByItemId(idItem);
            model.addAttribute("bid", bid);
            model.addAttribute("soldItem", soldItem);
            return "soldItem/details.html";
    }

    @GetMapping(value = ("/detail-item/{item_id}/modifier"))
    public String editSoldItem(@PathVariable(name = "item_id") String item_id,
                                  @ModelAttribute("categorySession") Category categorySession,
                                  @ModelAttribute("userSession") User userSession,
                                  Model model
    ) {
        int idItem = Integer.parseInt(item_id);
        SoldItem soldItem = soldItemService.getSoldItemById(idItem);
        Bid bid = bidService.getBidByItemId(idItem);
        model.addAttribute("bid", bid);
        model.addAttribute("soldItem", soldItem);
        return "soldItem/edit";
    }

    @GetMapping(value = "/detail-item/{item_id}/supprimer")
    public String deleteItem(Principal principal,
                            @PathVariable(name = "item_id") String item_id,
                            @ModelAttribute("userSession") User userSession,
                            Model model,
                            RedirectAttributes redirectAttributes
    ) {
        String currentUserName = principal.getName();
        int idItem = Integer.parseInt(item_id);
        SoldItem soldItem = soldItemService.getSoldItemById(idItem);
        soldItemService.delete(soldItem);
        redirectAttributes.addFlashAttribute("successMessage", "Your auction have been deleted");
        return "redirect:/encheres/"; // Redirect to a page showing all bids

    }

    private void savePicture(MultipartFile file, String currentUserName, Long itemNb) throws IOException {
        // Define the directory where you want to save the file
        String uploadDir = Paths.get("src/main/resources/static/images/", currentUserName).toString();

        // If the directory doesn't exist, create it
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // Construct the new filename with item number
        String originalFilename = file.getOriginalFilename();
        String newFilename = "image" + itemNb + originalFilename.substring(originalFilename.lastIndexOf('.'));

        // Save the file to the defined directory
        Path filePath = Paths.get(uploadDir, newFilename);
        Files.write(filePath, file.getBytes());
    }

    @GetMapping("/search")
    public String search(
        @ModelAttribute("userSession") User userSession,
        @RequestParam(name = "filters", required = false) String filters,
        @RequestParam(name = "category", required = false) Integer category,
        @RequestParam(name = "openBids", required = false) Integer openBids,
        @RequestParam(name = "myCurrentBids", required = false) Integer myCurrentBids,
        @RequestParam(name = "wonBids", required = false) Integer wonBids,
        @RequestParam(name = "currentSale", required = false) Integer currentSale,
        @RequestParam(name = "salesNotStarted", required = false) Integer salesNotStarted,
        @RequestParam(name = "completedSales", required = false) Integer completedSales,
        Model model)
    {
        if (category == null) {
            category = -1;
        }

        // Récupère la liste d'enchères filtrée en fonction des critères de recherche
        List<SoldItem> soldItems = soldItemService.search(filters, category, userSession.getUserNb(), openBids, myCurrentBids, wonBids, currentSale, salesNotStarted, completedSales);

        // Ajoute la liste d'enchères filtrée au modèle
        model.addAttribute("soldItems", soldItems);

        return "fragments/fragment-list-bids";
    }



    @ModelAttribute("categorySession")
    public List<Category> leadSessionCategory() {
        List<Category>categories = categoryService.getCategories();
        return categories;
    }
}
