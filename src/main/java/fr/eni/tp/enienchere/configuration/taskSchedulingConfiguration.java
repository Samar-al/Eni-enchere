package fr.eni.tp.enienchere.configuration;

import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.Token;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import fr.eni.tp.enienchere.dal.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class taskSchedulingConfiguration {

    @Autowired
    SoldItemDAO soldItemDAO;

    @Autowired
    UserDAO userDAO;

    @Scheduled(cron = "0 1 0 * * ?")
    public void scheduledTask() {

        List<SoldItem> soldItems = soldItemDAO.findAll();
        LocalDate todayLD = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date todayD = Date.from(todayLD.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        for(SoldItem soldItem : soldItems) {

            String check = "Statut avant de l'item : " + soldItem.getItemNb() + " : " + soldItem.getSaleStatus();

            if (todayD.before(soldItem.getDateStartBid())) {
                soldItem.setSaleStatus(0);
                check += " ,après : "+ soldItem.getSaleStatus();
            } else if (todayD.after(soldItem.getDateEndBid())) {
                soldItem.setSaleStatus(2);
                check += " ,après : "+ soldItem.getSaleStatus();
            } else {
                soldItem.setSaleStatus(1);
                check += " ,après : "+ soldItem.getSaleStatus();
            }

//            System.out.println(check);
            soldItemDAO.update(soldItem);

            List<Token> tokens = userDAO.findAllTokens();
            LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            for (Token token : tokens) {
                if (today.isAfter(token.getExpiryDate())){
                    userDAO.deleteTokenUser(token);
//                    System.out.println("executé");
                }
            }
        }

    }
}
