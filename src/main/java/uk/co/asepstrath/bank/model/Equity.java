package uk.co.asepstrath.bank.model;

public class Equity {
    private String name;
    private String ticker;
    private String sector;

    public Equity(String name, String ticker, String sector) {
        this.name = name;
        this.ticker = ticker;
        this.sector = sector;
    }

    public Equity() {
        this.name = "";
        this.ticker = "";
        this.sector = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
