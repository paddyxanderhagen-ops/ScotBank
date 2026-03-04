package uk.co.asepstrath.bank.model;

import java.math.BigDecimal;

public class Price {
    private String timestamp;
    private BigDecimal price;

    public Price(String timestamp, BigDecimal price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public Price() {
        this.timestamp = "";
        this.price = BigDecimal.valueOf(0);
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
