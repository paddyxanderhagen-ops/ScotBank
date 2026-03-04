package uk.co.asepstrath.bank.model;

import java.math.BigDecimal;

public class Price {
    private String timeStamp;
    private BigDecimal price;

    public Price(String timeStamp, BigDecimal price) {
        this.timeStamp = timeStamp;
        this.price = price;
    }

    public Price() {
        this.timeStamp = "";
        this.price = BigDecimal.valueOf(0);
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
