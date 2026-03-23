package uk.co.asepstrath.bank.model;

public class TaxSaleLine {
    private String ticker;
    private String timestamp;
    private double proceeds;
    private double costBasis;
    private double gainLoss;

    public TaxSaleLine(String ticker, String timestamp, double proceeds, double costBasis) {
        this.ticker = ticker;
        this.timestamp = timestamp;
        this.proceeds = Math.round(proceeds * 100.0) / 100.0;
        this.costBasis = Math.round(costBasis * 100.0) / 100.0;
        this.gainLoss = Math.round((proceeds - costBasis) * 100.0) / 100.0;
    }

    public String getTicker() { return ticker; }
    public String getTimestamp() { return timestamp; }
    public double getProceeds() { return proceeds; }
    public double getCostBasis() { return costBasis; }
    public double getGainLoss() { return gainLoss; }
}