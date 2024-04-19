package fr.eni.tp.enienchere.controller.converter;

import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSoldItemConverter implements Converter<String, SoldItem> {
    private SoldItemService soldItemService;

    public StringToSoldItemConverter(SoldItemService soldItemService) {
        this.soldItemService = soldItemService;
    }
    @Override
    public SoldItem convert(String id) {
        int soldItemId = Integer.parseInt(id);
        return soldItemService.getSoldItemById(soldItemId);
    }
}
