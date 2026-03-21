package uk.co.asepstrath.bank.model;

public class Position {
    private String ticker;
    private double shares;
    private double currPrice;
    private double costBasis;
    private double currentValue;
    private double pnl;
    private double pnlPercentage;
    private double realisedPnl;
    private double avgBuy;
    private double totalInvested;

    public Position(String ticker,
                    double shares,
                    double currPrice,
                    double costBasis,
                    double currentValue,
                    double realisedPnl,
                    double avgBuy,
                    double totalInvested
    )
    {
        this.ticker = ticker;
        this.shares = Math.round(shares * 100.0) / 100.0;
        this.currPrice = currPrice;
        this.costBasis = costBasis;
        this.currentValue = Math.round(currentValue * 100.0) / 100.0;
        this.realisedPnl = Math.round(realisedPnl * 100.0) / 100.0;
        this.avgBuy = Math.round(avgBuy * 100.0) / 100.0;

        double unrealisedPnl = currentValue - costBasis;
        this.pnl = Math.round(unrealisedPnl * 100.0) / 100.0;
        this.pnlPercentage = costBasis > 0
                ? Math.round((unrealisedPnl / costBasis * 100) * 100.0) / 100.0
                : 0;
        this.totalInvested = totalInvested;
    }

    public double getRealisedPnl() { return realisedPnl; }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getCurrPrice() {
        return currPrice;
    }

    public double getCostBasis() {
        return costBasis;
    }

    public double getPnl() {
        return pnl;
    }

    public double getPnlPercentage() {
        return pnlPercentage;
    }
    public String getTicker() {
        return ticker;
    }
    public double getShares() {
        return shares;
    }

    public double getAvgBuy () {
        return avgBuy;
    }
    public double getTotalInvested () {return totalInvested;}
}
