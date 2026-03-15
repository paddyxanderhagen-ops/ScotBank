package uk.co.asepstrath.bank.model;

public class Position {
    private String ticker;
    private double numberOfShares;
    private double buyPrice;
    private double lastPrice;
    private double buyCashAmount;
    private double currentCashAmount;
    private double pnl;
    private double pnlPercentage;

    public Position() {
    }

    public Position(String ticker, double numberOfShares, double buyPrice) {
        this.ticker = ticker;
        this.numberOfShares = numberOfShares;
        this.buyPrice = buyPrice;
    }

    public Position(String ticker,
        double numberOfShares,
        double buyPrice,
        double lastPrice,
        double buyCashAmount,
        double currentCashAmount)
    {
        this.ticker = ticker;
        this.numberOfShares = numberOfShares;
        this.buyPrice = buyPrice;
        this.lastPrice = lastPrice;
        this.buyCashAmount = buyCashAmount;
        this.currentCashAmount = currentCashAmount;
        this.pnl = Math.round((currentCashAmount - buyCashAmount) * 100.0) / 100.0;
        this.pnlPercentage = Math.round((100 - (currentCashAmount / buyCashAmount) * 100) * 100.0) / 100.0;

    }

    public double getCurrentCashAmount() {
        return currentCashAmount;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public double getBuyCashAmount() {
        return buyCashAmount;
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
    public double getNumberOfShares() {
        return numberOfShares;
    }
    public double getBuyPrice() {
        return buyPrice;
    }

}
