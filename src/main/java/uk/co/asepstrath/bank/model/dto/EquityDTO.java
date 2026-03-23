package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Equity;

import java.util.List;

public class EquityDTO {
    private List<Equity> assets;

    public List<Equity> getEquities() {
        return this.assets;
    }
}
