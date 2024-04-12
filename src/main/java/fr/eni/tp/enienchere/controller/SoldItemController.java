package fr.eni.tp.enienchere.controller;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bll.CategoryService;

import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.User;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.*;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/encheres")
@SessionAttributes({"categorySession"})
public class SoldItemController {

    private SoldItemService soldItemService;
    private CategoryService categoryService;

    private UserService userService;

    public SoldItemController(SoldItemService soldItemService, CategoryService categoryService, UserService userService) {
        this.soldItemService = soldItemService;
        this.categoryService = categoryService;
        this.userService = userService;
    }



    @GetMapping(value = "/")
    public String displayAllSoldItem(@ModelAttribute("categorySession") Category categorySession, Model model) {
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
                                    @RequestParam("picture") MultipartFile file) {

        if (principal == null) {
            return "redirect:/encheres/";
        } else {
            String currentUserName = principal.getName();
            if (file.isEmpty()) {
                // Handle empty file
                return "redirect:/error";
            }

            if (bindingResult.hasErrors()) {
                return "soldItem/create.html";
            } else {
                try {


                    // Continue with your business logic
                    Long newItemId = soldItemService.create(soldItem, currentUserName);
                    savePicture(file, currentUserName, newItemId);
                    return "redirect:/encheres/";
                } catch (IOException e) {
                    // Handle file writing exception
                    e.printStackTrace();
                    return "redirect:/error";
                }
            }
        }
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
