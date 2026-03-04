package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Price;

import java.util.List;

public class PriceDTO {
    private List<Price> priceList;
    public List<Price> getPriceList () {
        return this.priceList;
    }
}
