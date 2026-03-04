package uk.co.asepstrath.bank.model;

import java.util.ArrayList;
import java.util.List;

public class Etf {
    private String name;
    private String ticker;
    private String description;
    private String inceptionDate;
    private List<String> constituents;

    public Etf(String name, String ticker, String description, String inceptionDate, List<String> constituents) {
        this.name = name;
        this.ticker = ticker;
        this.description = description;
        this.inceptionDate = inceptionDate;
        this.constituents = constituents;
    }

    public Etf(){
        name = "";
        ticker = "";
        description = "";
        inceptionDate = "";
        constituents = new ArrayList<String>();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(String inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public List<String> getConstituents() {
        return constituents;
    }

    public void setConstituents(List<String> constituents) {
        this.constituents = constituents;
    }
}
