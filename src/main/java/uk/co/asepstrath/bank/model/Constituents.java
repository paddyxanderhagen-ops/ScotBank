package uk.co.asepstrath.bank.model;

import java.math.BigDecimal;

public class Constituents {
    private String ticker;
    private double weight;

    public Constituents(String ticker, double weight) {
        this.ticker = ticker;
        this.weight = weight;
    }

    public Constituents() {
        ticker = "";
        weight = 0;
    }
    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
