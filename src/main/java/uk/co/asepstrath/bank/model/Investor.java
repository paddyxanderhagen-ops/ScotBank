package uk.co.asepstrath.bank.model;

import java.util.ArrayList;
import java.util.List;

public class Investor {
    private final ArrayList<Stock> stocks;
    private String id;
    private String name;
    private double cashBalance;

    public Investor(String id, String name, double cashBalance) {
        this.id = id;
        this.name = name;
        this.cashBalance = cashBalance;
        stocks = new ArrayList<>();
    }

    public Investor() {
        id = "0";
        name = "";
        cashBalance = 0.0;
        stocks = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Stock stock) {
        stocks.add(stock);
    }
}
