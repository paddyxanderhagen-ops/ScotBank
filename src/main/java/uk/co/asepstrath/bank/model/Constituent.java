package uk.co.asepstrath.bank.model;

public class Constituent {
    private String ticker;
    private double weight;

    public Constituent(String ticker, double weight) {
        this.ticker = ticker;
        double unroundedWeight = weight*100;
        this.weight = Math.round(unroundedWeight * 100.0) / 100.0;
    }

    public Constituent() {
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
