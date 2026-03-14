package uk.co.asepstrath.bank.model;

public class Transaction {
    private String id;
    private String timestamp;
    private String type;
    private String investorId;
    private String ticker;
    private double totalCashAmount;

    public Transaction(String id, String timestamp, String type, String investorId, String ticker, double totalCashAmount) {
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
        this.investorId = investorId;
        this.ticker = ticker;
        this.totalCashAmount = totalCashAmount;
    }

    public Transaction() {
        id = "";
        timestamp = "";
        type = "";
        investorId = "";
        ticker = "";
        totalCashAmount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInvestorId() {
        return investorId;
    }

    public void setInvestorId(String investorId) {
        this.investorId = investorId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getTotalCashAmount() {
        return totalCashAmount;
    }

    public void setTotalCashAmount(double totalCashAmount) {
        this.totalCashAmount = totalCashAmount;
    }
}
