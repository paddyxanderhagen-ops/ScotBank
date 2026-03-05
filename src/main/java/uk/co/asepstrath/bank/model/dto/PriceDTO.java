package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Price;

import java.util.List;

public class PriceDTO {
    private List<Price> prices;
    public List<Price> getPriceList () {
        return this.prices;
    }
}
