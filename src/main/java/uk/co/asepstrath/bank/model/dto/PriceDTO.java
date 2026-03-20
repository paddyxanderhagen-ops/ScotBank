package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Pagination;
import uk.co.asepstrath.bank.model.Price;

import java.util.List;

public class PriceDTO {
    private Pagination pagination;
    private List<Price> prices;

    public List<Price> getPrices () {
        return this.prices;
    }

    public void setPrices (List<Price> prices) {this.prices = prices;}

    public Pagination getPages () {
        return this.pagination;
    }
}
