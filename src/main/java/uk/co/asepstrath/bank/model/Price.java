package uk.co.asepstrath.bank.model;

import java.math.BigDecimal;

public class Price {
    private String timestamp;
    private String ticker;
    private BigDecimal price;

    public Price(String timestamp, BigDecimal price, String ticker) {
        this.timestamp = timestamp;
        this.price = price;
        this.ticker = ticker;
    }

    public Price() {
        this.timestamp = "";
        this.price = BigDecimal.valueOf(0);
        this.ticker = "";
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
    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
