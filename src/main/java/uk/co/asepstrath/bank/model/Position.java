package uk.co.asepstrath.bank.model;

public class Position {
    private String ticker;
    private int numberOfShares;
    private double buyPrice;
    private double lastPrice;
    private double buyCashAmount;
    private double currentCashAmount;
    private double pnl;

    public Position() {
    }

    public Position(String ticker, int numberOfShares, double buyPrice) {
        this.ticker = ticker;
        this.numberOfShares = numberOfShares;
        this.buyPrice = buyPrice;
    }

    public Position(String ticker,
                    int numberOfShares,
                    double buyPrice,
                    double lastPrice,
                    double buyCashAmount,
                    double currentCashAmount,
                    double pnl) {

        this.ticker = ticker;
        this.numberOfShares = numberOfShares;
        this.buyPrice = buyPrice;
        this.lastPrice = lastPrice;
        this.buyCashAmount = buyCashAmount;
        this.currentCashAmount = currentCashAmount;
        this.pnl = pnl;
    }
}
