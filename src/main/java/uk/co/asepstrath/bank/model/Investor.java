package uk.co.asepstrath.bank.model;

import java.math.BigDecimal;

public class Investor {
    private String id;
    private String name;
    private BigDecimal cashBalance;

    public Investor(String id, String name, BigDecimal cashBalance) {
        this.id = id;
        this.name = name;
        this.cashBalance = cashBalance;
    }

    public Investor() {
        id = "0";
        name = "";
        cashBalance = BigDecimal.valueOf(0);
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

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }
}

