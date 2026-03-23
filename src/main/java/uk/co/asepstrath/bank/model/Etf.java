package uk.co.asepstrath.bank.model;

import java.util.ArrayList;
import java.util.List;

public class Etf extends Instrument{
    private String description;
    private String inceptionDate;
    private List<Constituent> constituents;

    public Etf(String ticker, String name, String description, String inceptionDate, List<Constituent> constituents) {
        super(ticker, name);
        this.description = description;
        this.inceptionDate = inceptionDate;
        this.constituents = constituents;
    }

    public Etf() {
        super("", "");
        description = "";
        inceptionDate = "";
        constituents = new ArrayList<Constituent>();
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

    public List<Constituent> getConstituents() {
        return constituents;
    }

    public void setConstituents(List<Constituent> constituents) {
        this.constituents = constituents;
    }
}
