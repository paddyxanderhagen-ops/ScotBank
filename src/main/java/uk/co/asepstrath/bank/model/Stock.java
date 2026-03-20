package uk.co.asepstrath.bank.model;

public class Stock {

    private String ticker;
    private int amount;
    private double bookPrice;
    private boolean isBuy;

    public Stock(String ticker, int amount, double bookPrice, boolean isBuy) {
        this.ticker = ticker;
        this.amount = amount;
        this.bookPrice = bookPrice;
        this.isBuy = isBuy;
    }

    public Stock() {
        ticker = "";
        amount = 0;
        bookPrice = 0;
        isBuy = false;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return bookPrice;
    }

    public void setPrice(double price) {
        this.bookPrice = price;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean isBuy) {
        this.isBuy = isBuy;
    }
}
