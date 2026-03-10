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
            char[] chars = price.getTimeStamp().toCharArray();
            char[] chars2 = max.getTimeStamp().toCharArray();
            String temp = "";
            String temp2 = "";
            for (int i = 0; i < 4; i++) {
                temp = temp.concat(String.valueOf(chars[i]));
                temp2 = temp2.concat(String.valueOf(chars2[i]));
            }
            if (Integer.parseInt(temp) > Integer.parseInt(temp2)) {
                max = price;
            } else {
                temp = "";
                temp2 = "";
                for (int i = 5; i < 7; i++){
                    temp = temp.concat(String.valueOf(chars[i]));
                    temp2 = temp2.concat(String.valueOf(chars2[i]));
                }
                if (Integer.parseInt(temp) > Integer.parseInt(temp2)) {
                    max = price;
                } else {
                    temp = "";
                    temp2 = "";
                    for (int i = 8; i < 10; i++){
                        temp = temp.concat(String.valueOf(chars[i]));
                        temp2 = temp2.concat(String.valueOf(chars2[i]));
                    }
                    if (Integer.parseInt(temp) > Integer.parseInt(temp2)) {
                        max = price;
                    } else {
                        temp = "";
                        temp2 = "";
                        for (int i = 11; i < chars.length; i++){
                            temp = temp.concat(String.valueOf(chars[i]));
                            temp2 = temp2.concat(String.valueOf(chars2[i]));
                        }
                        temp = temp.replaceAll(":", "");
                        temp2 = temp2.replaceAll(":", "");
                        if (Integer.parseInt(temp) > Integer.parseInt(temp2)) {
                            max = price;
                        }
                    }
                }
            }
        }
        return max;
    }
}
