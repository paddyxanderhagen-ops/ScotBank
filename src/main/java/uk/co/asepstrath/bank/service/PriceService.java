package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Price;

import java.math.BigDecimal;
import java.util.List;

public class PriceService {
    public Price getByTimestamp(String timestamp, List<Price> priceList) {
        for (Price price : priceList) {
            if (price.getTimeStamp().equals(timestamp)) {
                return price;
            }
        }
        return null;
    }

    public Price getMostRecent(List<Price> priceList) {
        Price max = priceList.get(0);
        for (Price price : priceList) {
            char[] currentChars = price.getTimeStamp().toCharArray();
            char[] maxChars = max.getTimeStamp().toCharArray();
            String current = "";
            String stringMax = "";
            for (int i = 0; i < 4; i++) {
                current = current.concat(String.valueOf(currentChars[i]));
                stringMax = stringMax.concat(String.valueOf(maxChars[i]));
            }
            if (Integer.parseInt(current) > Integer.parseInt(stringMax)) {
                max = price;
            } else {
                current = "";
                stringMax = "";
                for (int i = 5; i < 7; i++){
                    current = current.concat(String.valueOf(currentChars[i]));
                    stringMax = stringMax.concat(String.valueOf(maxChars[i]));
                }
                if (Integer.parseInt(current) > Integer.parseInt(stringMax)) {
                    max = price;
                } else {
                    current = "";
                    stringMax = "";
                    for (int i = 8; i < 10; i++){
                        current = current.concat(String.valueOf(currentChars[i]));
                        stringMax = stringMax.concat(String.valueOf(maxChars[i]));
                    }
                    if (Integer.parseInt(current) > Integer.parseInt(stringMax)) {
                        max = price;
                    } else {
                        current = "";
                        stringMax = "";
                        for (int i = 11; i < currentChars.length; i++){
                            current = current.concat(String.valueOf(currentChars[i]));
                            stringMax = stringMax.concat(String.valueOf(maxChars[i]));
                        }
                        current = current.replaceAll(":", "");
                        stringMax = stringMax.replaceAll(":", "");
                        if (Integer.parseInt(current) > Integer.parseInt(stringMax)) {
                            max = price;
                        }
                    }
                }
            }
        }
        return max;
    }

    public BigDecimal getLatestPrice(List<Price> priceList) {
        return getMostRecent(priceList).getPrice();
    }

    public BigDecimal getPrice(String timestamp, List<Price> priceList) {
        Price price = getByTimestamp(timestamp, priceList);

        if (price == null) {
            throw new IllegalArgumentException("Price not found for timestamp: " + timestamp);
        }

        return price.getPrice();
    }

}
