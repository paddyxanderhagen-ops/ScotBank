package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class PriceService {

    public PriceService() {}

    public Price getByTimestamp(String timestamp, List<Price> priceList) {
        LocalDateTime target = LocalDateTime.parse(timestamp);

        return priceList.stream()
                .filter(p -> !LocalDateTime.parse(p.getTimeStamp()).isAfter(target))
                .max(Comparator.comparing(p -> LocalDateTime.parse(p.getTimeStamp())))
                .orElseGet(() -> priceList.stream()
                        .min(Comparator.comparing(p -> LocalDateTime.parse(p.getTimeStamp())))
                        .orElseThrow(() -> new RuntimeException("No prices available"))
                );
    }

    public Price getMostRecent(List<Price> priceList) {
        return priceList.stream()
                .max(Comparator.comparing(p -> LocalDateTime.parse(p.getTimeStamp())))
                .orElseThrow(() -> new RuntimeException("Price list is empty"));
    }

    public BigDecimal getLatestPrice(List<Price> priceList) {
        return getMostRecent(priceList).getPrice();
    }

    public BigDecimal getPrice(String timestamp, List<Price> priceList) {
        return getByTimestamp(timestamp, priceList).getPrice();
    }
}