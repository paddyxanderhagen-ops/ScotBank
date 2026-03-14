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

    public Position(String ticker, int numberOfShares, double buyPrice) {
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
        this.pnl = currentCashAmount - buyCashAmount;
        this.pnlPercentage = currentCashAmount / buyCashAmount * 100;
    }
}
