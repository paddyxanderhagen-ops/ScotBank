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
    private double realisedPnl;
    private double totalInvested;

    public Position(String ticker,
                    double numberOfShares,
                    double buyPrice,
                    double lastPrice,
                    double buyCashAmount,
                    double currentCashAmount,
                    double realisedPnl,
                    double totalInvested)
    {
        this.ticker = ticker;
        this.numberOfShares = numberOfShares;
        this.buyPrice = buyPrice;
        this.lastPrice = lastPrice;
        this.buyCashAmount = buyCashAmount;
        this.currentCashAmount = currentCashAmount;
        this.realisedPnl = Math.round(realisedPnl * 100.0) / 100.0;
        this.totalInvested = Math.round(totalInvested * 100.0) / 100.0;

        double unrealised = currentCashAmount - buyCashAmount;
        this.pnl = Math.round(unrealised * 100.0) / 100.0;
        this.pnlPercentage = buyCashAmount > 0
                ? Math.round((unrealised / buyCashAmount * 100) * 100.0) / 100.0
                : 0;
    }

    public double getTotalInvested() { return totalInvested; }
    public double getRealisedPnl() { return realisedPnl; }

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
