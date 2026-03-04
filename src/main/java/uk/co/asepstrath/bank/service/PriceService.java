package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Price;

import java.math.BigDecimal;
import java.util.List;

public class PriceService {
    public BigDecimal getByTimestamp(String timestamp, List<Price> priceList) {
        for (Price price : priceList) {
            if (price.getTimeStamp().equals(timestamp)) {
                return price.getPrice();
            }
        }
        return BigDecimal.valueOf(0);
    }
}
