package uk.co.asepstrath.bank.model;

import java.math.BigDecimal;

public class Constituents {
    private String ticker;
    private BigDecimal weight;

    public Constituents(String ticker, BigDecimal weight) {
        this.ticker = ticker;
        this.weight = weight;
    }

    public Constituents() {
        ticker = "";
        weight = BigDecimal.ZERO;
    }
    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public BigDecimal getWeight() {
        return weight;
    }
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
