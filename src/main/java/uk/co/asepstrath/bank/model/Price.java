package uk.co.asepstrath.bank.model;

public class Price {
    private String timestamp;
    private double price;

    public Price(String timestamp, double instrumentPrice) {
        this.timestamp = timestamp;
        this.price = instrumentPrice;
    }

    public Price() {
        this.timestamp = "";
        this.price = 0;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
