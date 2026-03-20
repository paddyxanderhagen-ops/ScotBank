package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Equity;

import java.util.List;

public class EquitiesDTO {
    private List<Equity> assets;

    public List<Equity> getEquities () {
        return this.assets;
    }
}
