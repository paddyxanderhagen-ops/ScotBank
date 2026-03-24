package uk.co.asepstrath.bank.model;

public class Equity extends Instrument {
    private String sector;

    public Equity(String ticker, String name, String sector) {
        super(ticker, name);
        this.sector = sector;
    }

    public Equity() {
        super("", "");
        this.sector = "";
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
