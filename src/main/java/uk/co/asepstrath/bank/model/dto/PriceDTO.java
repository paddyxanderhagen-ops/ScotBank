package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Pagination;
import uk.co.asepstrath.bank.model.Price;

import java.util.List;

public class PriceDTO {
    private Pagination pagination;
    private List<Price> prices;
    public List<Price> getPriceList () {
        return this.prices;
    }
    public Price addPrice (Price price) {
        this.prices.add(price);
        return price;
    }
    public Pagination getPages () {
        return this.pagination;
    }
}
