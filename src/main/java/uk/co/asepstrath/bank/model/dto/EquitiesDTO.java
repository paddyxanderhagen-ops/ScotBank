package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;

import java.util.List;

public class EquitiesDTO {
    private List<Equity> equities;

    public List<Equity> getEquities() {
        return this.equities;
    }
}
